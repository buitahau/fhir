package owt.training.fhir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import owt.training.fhir.constant.QueueNameConstant;
import owt.training.fhir.message_queue.MessageQueueService;
import owt.training.fhir.message_queue.dto.FhirMessage;

@RestController
@RequestMapping("/request-resource")
public class RequestResourceController {

    @Autowired
    private MessageQueueService messageQueueService;

    @GetMapping("/get/{resourceName}/{id}")
    public void getResource(@PathVariable("resourceName") String resourceName,
                            @PathVariable("id") String id) {

        FhirMessage message = FhirMessage
                .builder()
                .action("GET")
                .resourceName(resourceName)
                .body(id)
                .build();

        messageQueueService.sendToQueue(QueueNameConstant.RESOURCE_REQUEST, message);
    }

    @GetMapping("/callback/get/{resourceName}/{id}")
    public void getResourceCallback(@PathVariable("resourceName") String resourceName,
                                    @PathVariable("id") String id,
                                    @RequestParam("callbackUrl") String callbackUrl) {

        FhirMessage message = FhirMessage
                .builder()
                .action("GET")
                .resourceName(resourceName)
                .callbackUrl(callbackUrl)
                .body(id)
                .build();

        messageQueueService.sendToQueue(QueueNameConstant.RESOURCE_REQUEST, message);
    }
}
