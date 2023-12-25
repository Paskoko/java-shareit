package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Class with item's components
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;
    private String name;
    private String description;
    @Column(name = "owner_id")
    private int ownerId;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "request_id")
    private int requestId;
    @Transient
    private List<Comment> commentList;

    public Item compare(Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName() != null ? item.getName() : this.getName())
                .description(item.getDescription() != null ?
                        item.getDescription() : this.getDescription())
                .ownerId(item.getOwnerId() != 0 ? item.getOwnerId() : this.getOwnerId())
                .isAvailable(item.getIsAvailable() != null ?
                        item.getIsAvailable() : this.getIsAvailable())
                .requestId(item.getRequestId() != 0 ? item.getRequestId() : this.getRequestId())
                .commentList(item.getCommentList() != null ?
                        item.getCommentList() : this.getCommentList())
                .build();
    }
}
