package org.example.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.Service.Impl.CartServiceImpl;
import org.example.ecommerce.Service.Interfaces.CartService;
import org.example.ecommerce.Utils.ResponseUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    private ObjectMapper mapper;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int userId = getUserId(req);
        ResponseUtil.sendJson(req,res, 200, cartService.getCartByUserId(userId));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int userId = getUserId(req);
        Map<String, Integer> body = mapper.readValue(req.getInputStream(), Map.class);
        int productId = body.get("productId");
        int quantity  = body.getOrDefault("quantity", 1);
        ResponseUtil.sendJson(req,res, 201, cartService.addToCart(userId, productId, quantity));
    }

    // PUT /cart/{id}  { quantity }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int cartId = Integer.parseInt(req.getPathInfo().substring(1));
        if(cartId<0){
            ResponseUtil.sendError(req,res,400,"Invalid ID");
        }
        Map<String, Integer> body = mapper.readValue(req.getInputStream(), Map.class);
        ResponseUtil.sendJson(req,res, 200, cartService.updateCartItem(cartId, body.get("quantity")));
    }

    // DELETE /cart/{id}
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int cartId = Integer.parseInt(req.getPathInfo().substring(1));
        if(cartId<0){
            ResponseUtil.sendError(req,res,400,"Invalid ID");
        }
        if (cartService.removeFromCart(cartId)) ResponseUtil.sendSuccess(req,res, "Item removed");
        else ResponseUtil.sendError(req,res, 404, "Cart item not found");
    }

    private int getUserId(HttpServletRequest req) {
        return (int) req.getAttribute("userId");
    }
}
