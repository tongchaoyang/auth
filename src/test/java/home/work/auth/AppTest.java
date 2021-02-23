package home.work.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest 
{
    public AppTest()
    {
    }

    @Test
    public void parsing()
    {
        // wrong msg indicator
        assertThrows(IllegalArgumentException.class, () ->  {new Request("0110e016411111111111111112250000001000");});
        // shorter pan length
        assertThrows(IllegalArgumentException.class, () ->  {new Request("0100e010411111111111111112250000001000");});
        // longer pan length
        assertThrows(IllegalArgumentException.class, () ->  {new Request("0100e020411111111111111112250000001000");});
        // missing pan
        assertThrows(IllegalArgumentException.class, () ->  {new Request("01006012250000001000");});
        // missing expiration in bitmap
        assertThrows(IllegalArgumentException.class, () ->  {new Request("0100a01041111111111111110000001000");});
        // missing amount in bitmap
        assertThrows(IllegalArgumentException.class, () ->  {new Request("0100c01041111111111111110000001000");});
        // invalid amount
        assertThrows(IllegalArgumentException.class, () ->  {new Request("0100e016411111111111111112250000000000");});

        Request req = new Request("0100e016411111111111111112250000001000");
        assertTrue("4111111111111111".equals(req.getPan()));
        assertTrue("1225".equals(req.getExp()));
        assertTrue("0000001000".equals(req.getAmt()));
        assertNull(req.getName());
        assertNull(req.getZip());
        req = new Request("0100ec1651051051051051001225000001100011MASTER YODA90089");
        assertTrue("5105105105105100".equals(req.getPan()));
        assertTrue("1225".equals(req.getExp()));
        assertTrue("0000011000".equals(req.getAmt()));
        assertTrue("MASTER YODA".equals(req.getName()));
        assertTrue("90089".equals(req.getZip()));
    }
    
    @Test
    public void compose()
    {
        // negative tests
        // amount = 0
        Response rsp = new Response("0100e016411111111111111112250000000000");
        assertTrue(rsp.toString().equals("0110f016411111111111111112250000000000ER"));
        
        // missing pan
        rsp = new Response("01006012250000001000");
        assertTrue(rsp.toString().equals("01107012250000001000ER"));
        
        // amount = 0
        rsp = new Response("0100ec1651051051051051001225000000000011MASTER YODA90089");
        assertTrue(rsp.toString().equals("0110fc16510510510510510012250000000000ER11MASTER YODA90089"));
        
        // amount = 100, no zip
        Request req = new Request("0100e016411111111111111112250000010000");
        rsp = new Response(req, Decision.DE);
        assertTrue(rsp.toString().equals("0110f016411111111111111112250000010000DE"));
        
        // amount = 110, no zip
        req = new Request("0100e81651051051051051001225000001100011MASTER YODA");
        rsp = new Response(req, Decision.DE);
        assertTrue(rsp.toString().equals("0110f816510510510510510012250000011000DE11MASTER YODA"));
        
        rsp = new Response("0100ec1651051051051051001225000000000011MASTER YODA90089");
        assertTrue(rsp.toString().equals("0110fc16510510510510510012250000000000ER11MASTER YODA90089"));

        // positive tests
        // amount =10
        req = new Request("0100e016411111111111111112250000001000");
        rsp = new Response(req, Decision.OK);
        assertTrue(rsp.toString().equals("0110f016411111111111111112250000001000OK"));

        // amount = 110 with zip
        req = new Request("0100ec1651051051051051001225000001100011MASTER YODA90089");
        rsp = new Response(req, Decision.OK);
        assertTrue(rsp.toString().equals("0110fc16510510510510510012250000011000OK11MASTER YODA90089"));
    }
}
