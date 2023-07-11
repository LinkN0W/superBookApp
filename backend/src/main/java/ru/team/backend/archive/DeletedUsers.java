package ru.team.backend.archive;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ru.team.backend.entities.User;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "deleted_users")
@NoArgsConstructor
public class DeletedUsers implements DeletedEntities{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "remover_id")
    private UUID removerId;


    @Column(name = "date_of_delete")
    private Date dateOfDelete;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public DeletedUsers(UUID removerId, Date dateOfDelete, User user) {
        this.removerId = removerId;
        this.dateOfDelete = dateOfDelete;
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getRemoverId() {
        return removerId;
    }

    @Override
    public Date getDateOfDelete() {
        return dateOfDelete;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;

    }

    @Override
    public void setRemoverId(UUID removerId) {
        this.removerId = removerId;

    }

    @Override
    public void setDateOfDelete(Date dateOfDelete) {
        this.dateOfDelete = dateOfDelete;

    }
}
