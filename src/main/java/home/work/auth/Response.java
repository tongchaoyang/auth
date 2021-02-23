package home.work.auth;

public class Response {
    String msg;
    public final String RSP = "0110";
    
    public Response(Request req, Decision d) {
        StringBuilder sb = new StringBuilder();
        sb.append(RSP);
        short bm = 0xf0;
        if (req.getName() != null) {
            bm += 8;
        }
        if (req.getZip() != null) {
            bm += 4;
        }
        sb.append(String.format("%2x", bm));
        
        sb.append(String.format("%2d", req.getPan().length()));
        sb.append(req.getPan());
        sb.append(req.getExp());
        sb.append(req.getAmt());
        sb.append(d.name());
        if (req.getName() != null) {
            sb.append(String.format("%2d", req.getName().length()));
            sb.append(req.getName());
        }        
        if (req.getZip() != null) {
            sb.append(req.getZip());
        }
        msg = sb.toString();
    }
    
    // error message
    public Response(String req) {
        short bm = Short.parseShort(req.substring(4, 6), 16);
        bm += 0x10;
        String data = req.substring(6);
        int panLen;
        int optionalIdx;
        if ((bm & 0x80) == 0) {
            panLen = 0;
            optionalIdx = 4 + 10;
        } else {
            panLen = Integer.parseInt(data.substring(0, 2));
            optionalIdx = 2 + panLen + 4 + 10;
        }
        String optional = data.substring(optionalIdx);

        StringBuilder sb = new StringBuilder();
        sb.append(RSP);
        sb.append(String.format("%2x", bm));
        sb.append(data.subSequence(0, optionalIdx));
        sb.append(Decision.ER.name());
        sb.append(optional);
        msg = sb.toString();
    }
    
    @Override
    public String toString() {
        return msg;
    }
}
