create table public.question
(
    question_id        integer primary key not null,
    score              integer             not null,
    link               text                not null,
    answer_count       integer             not null,
    view_count         integer             not null,
    content_license    text,
    title              text                not null,
    last_activity_date timestamp without time zone not null,
    last_edit_date     timestamp without time zone,
    creation_date      timestamp without time zone not null,
    account_id         integer             not null,
    body               text                not null
);

create table public.answer
(
    answer_id          integer primary key not null,
    last_activity_date timestamp without time zone not null,
    last_edit_date     timestamp without time zone,
    creation_date      timestamp without time zone not null,
    score              integer             not null,
    is_accepted        boolean             not null,
    content_license    text,
    question_id        integer             not null,
    body               text                not null,
    account_id         integer             not null,
    foreign key (question_id) references public.question (question_id)
        match simple on update no action on delete no action
);

create table public.comment
(
    comment_id      integer primary key not null,
    edited          boolean             not null,
    post_id         integer             not null,
    body            text                not null,
    creation_date   timestamp without time zone not null,
    score           integer             not null,
    content_license text,
    account_id      integer             not null,
    foreign key (post_id) references public.answer (answer_id)
        match simple on update no action on delete no action
);

create table public.tag
(
    tag_id integer primary key not null,
    tag    text                not null
);

create table public.question_tag
(
    id          integer primary key not null,
    tag_id      integer             not null,
    question_id integer             not null,
    foreign key (tag_id) references public.tag (tag_id)
        match simple on update no action on delete no action,
    foreign key (question_id) references public.question (question_id)
        match simple on update no action on delete no action
);


