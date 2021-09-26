package com.xw.springbootwebsocket.controller;

import com.xw.springbootwebsocket.modal.MsgSendDTO;
import com.xw.springbootwebsocket.vertx.VertxServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1.0/dispatch")
public class DispatchController {


    @Autowired
    private VertxServer vertxServer;

    @GetMapping("send")
    public Map<String, Object> send(@RequestParam("receiver") String receiver,
                                    @RequestParam("content") String content) {
        vertxServer.send(receiver, content);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "success");
        response.put("data", null);
        return response;
    }

}
