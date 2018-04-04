package app.btz.function.notes.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by User on 2017/7/25.
 */
@Entity
@Table(name = "btz_notes_thumbsUp_table")
public class ThumbsUpEntity  implements Serializable{

    private Integer id;

    private Integer notesId;

    private Integer userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 20)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "notesId", nullable = false, length = 20)
    public Integer getNotesId() {
        return notesId;
    }

    public void setNotesId(Integer notesId) {
        this.notesId = notesId;
    }

    @Column(name = "userId", nullable = false, length = 20)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
