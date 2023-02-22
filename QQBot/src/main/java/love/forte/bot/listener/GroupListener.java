package love.forte.bot.listener;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import org.springframework.stereotype.Component;


@Component
public class GroupListener {

    @Listener
    @Filter(targets = @Filter.Targets(groups = {"114156145"}))
    public void groupMessage(GroupMessageEvent event) {
        String content = event.getMessageContent().getPlainText();
        event.replyBlocking(HelloListener.getAnswer(content));
    }

}
