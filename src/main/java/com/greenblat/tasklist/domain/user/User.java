package com.greenblat.tasklist.domain.user;

import com.greenblat.tasklist.domain.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    @Transient
    private String passwordConfirmation;

    @Column(name = "role")
    @ElementCollection
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @CollectionTable(name = "users_tasks")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private List<Task> tasks;


}
