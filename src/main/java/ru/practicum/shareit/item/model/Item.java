package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Class with item's components
 */
@Data
@Builder
public class Item {
    private int id;
    private String name;
    private String description;
    private int ownerId;
    private Boolean isAvailable;
    private ItemRequest request;
    private List<Feedback> feedbackList;

    public Item compare(Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName() != null ? item.getName() : this.getName())
                .description(item.getDescription() != null ?
                        item.getDescription() : this.getDescription())
                .ownerId(item.getOwnerId() != 0 ? item.getOwnerId() : this.getOwnerId())
                .isAvailable(item.getIsAvailable() != null ?
                        item.getIsAvailable() : this.getIsAvailable())
                .request(item.getRequest() != null ? item.getRequest() : this.getRequest())
                .feedbackList(item.getFeedbackList() != null ?
                        item.getFeedbackList() : this.getFeedbackList())
                .build();
    }
}
