package model.entity;

import lombok.*;
import model.enums.State;
import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tasks", schema = "todo_app")
public class TaskEntity implements aEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state = State.NOT_STARTED;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "task_id")
    @ToString.Exclude
    private Set<CommentEntity> comments = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name ="tasks_to_tags", schema = "todo_app",
            joinColumns =  @JoinColumn(name="task_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    @ToString.Exclude
    private Set<TagEntity> tags = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TaskEntity that = (TaskEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
