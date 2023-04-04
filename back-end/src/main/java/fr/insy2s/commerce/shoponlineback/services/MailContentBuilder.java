package fr.insy2s.commerce.shoponlineback.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {
    final private TemplateEngine templateEngine;
    String htmlBuilderContent (String token, String template){
        Context context = new Context();
        context.setVariable("token",token);
        return templateEngine.process(template +".html", context);
    }
}
