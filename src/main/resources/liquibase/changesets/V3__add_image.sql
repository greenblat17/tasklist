create table if not exists tasks_images
(
    task_id bigint       not null,
    image   varchar(255) not null,
    constraint fk_tasks_images_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);