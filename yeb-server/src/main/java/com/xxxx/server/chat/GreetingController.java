package com.xxxx.server.chat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.AssertUtil;
import com.xxxx.server.vo.ChatMsg;
import com.xxxx.server.vo.WebMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hxxiapgy
 */
@RestController
public class GreetingController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private IAdminService adminService;


    @MessageMapping("/ws/chat")
    public void greeting(Principal principal, WebMessage webMessage) throws Exception {

        Admin admin = null;

        if (principal != null) {
            admin = adminService.getOne(new QueryWrapper<Admin>()
                    .eq("username", principal.getName()));
        }

        // 用户为登录
        AssertUtil.isTrue(null == admin, "请登录！");

        // 创建返回对象
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setTo(webMessage.getTo());
        chatMsg.setContent(webMessage.getContent());
        chatMsg.setDate(LocalDateTime.now());
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFromNickName(admin.getUsername());

        // 发送消息
        simpMessagingTemplate.convertAndSendToUser(
                webMessage.getTo(),
                "/queue/chat",
                chatMsg);
    }

    @GetMapping("/chat/admin")
    public List<Admin> getAdminList() {
        return adminService.selectAdminList(null);
    }
}
