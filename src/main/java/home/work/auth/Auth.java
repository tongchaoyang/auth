package home.work.auth;

public class Auth {
    static final String AMT200 = "0000020000";
    static final String AMT100 = "0000010000";

    public static Response process(Request req) {
        if (req.getAmt().compareTo(AMT200) >= 0 || req.cardExpired()) {
            return new Response(req, Decision.DE);
        }
        
        if (req.getAmt().compareTo(AMT100) >= 0 && req.getZip() != null) {
            return new Response(req, Decision.DE);
        }
        
        return new Response(req, Decision.OK);
    }
}
