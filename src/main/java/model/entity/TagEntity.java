package model.entity;

import lombok.*;
import model.enums.Color;
import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tags", schema = "todo_app")
public class TagEntity implements aEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name ="tasks_to_tags", schema = "todo_app",
            joinColumns =  @JoinColumn(name="tag_id"),
            inverseJoinColumns = @JoinColumn(name="task_id"))
    @ToString.Exclude
    private Set<TaskEntity> tasks = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TagEntity tagEntity = (TagEntity) o;
        return getId() != null && Objects.equals(getId(), tagEntity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
