package home.work.auth;

import java.time.LocalDate;

public class Request {
    private String pan;
    private String exp;
    private String name;
    private String zip;
    private String amt;
    private Boolean expired;
    private String msg;
    
    static final String AUTH = "0100";
    static final int BM_POS = 4;
    static final int PAN_POS = 6;

    public Request(String msg) {
        this.msg = msg;
        if (!msg.substring(0, 4).equals(AUTH)) {
            throw new IllegalArgumentException("Illegal message: " + msg);
        }
        short map = Short.parseShort(msg.substring(BM_POS,  BM_POS + 2), 16);
        short bit = 0x80;
        int idx = 0;
        if ((map & bit) == 0) {
            throw new IllegalArgumentException("Missing PAN field: " + msg);
        } else {
            int len = Integer.parseInt(msg.substring(PAN_POS, PAN_POS + 2));
            if (len < 14 || len > 19) {
                throw new IllegalArgumentException("Invalid PAN field: " + msg);
            }
            idx = PAN_POS + 2 + len;
            pan = msg.substring(PAN_POS + 2, idx);
        }
        
        bit = 0x40;
        if ((map & bit) == 0) {
            throw new IllegalArgumentException("Missing expiration field: " + msg);
        } else {
            exp = msg.substring(idx, idx + 4);
            idx += 4;
        }
        
        bit = 0x20;
        if ((map & bit) == 0) {
            throw new IllegalArgumentException("Missing amount field: " + msg);
        } else {
            amt = msg.substring(idx, idx + 10);
            if(Integer.parseInt(amt) <= 0) {
                throw new IllegalArgumentException("Invalid amount field: " + msg);
            }
            idx += 10;
        }
        
        bit = 8;
        if ((map & bit) > 0) {
            int len = Integer.parseInt(msg.substring(idx, idx + 2));
            idx += 2;
            name = msg.substring(idx, idx + len);
            idx += len;
        }
        
        bit = 4;
        if ((map & bit) > 0) {
            zip = msg.substring(idx);
        }
    }

    public boolean cardExpired() {
        if (expired != null)
            return expired;
        
        LocalDate today = LocalDate.now();
        // assume cards expires within at most 10 years
        int maxYear = today.getYear() + 10;
        int year = 2000 + Integer.parseInt(exp.substring(2));
        if (year < today.getYear() || year > maxYear) {
            expired = true;
        } else {
            int month = today.getMonthValue();
            if (month > Integer.parseInt(exp.substring(0, 2))) {
                expired = true;
                return true;
            }
            expired = false;
        }
        
        return expired;
    }

    public String getPan() {
        return pan;
    }

    public String getExp() {
        return exp;
    }

    public String getName() {
        return name;
    }

    public String getZip() {
        return zip;
    }

    public String getAmt() {
        return amt;
    }
}
