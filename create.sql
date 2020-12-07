create table address_entity (id integer not null auto_increment, city varchar(255), number integer not null, street varchar(255), primary key (id)) engine=InnoDB
create table game_entity (id integer not null auto_increment, title varchar(255), type varchar(255), primary key (id)) engine=InnoDB
create table person (id bigint not null auto_increment, date_of_birth date, person_name varchar(50) not null, address_id integer, primary key (id)) engine=InnoDB
create table played_by (games_id integer not null, persons_id bigint not null) engine=InnoDB
create table telephone_number_entity (id integer not null auto_increment, number varchar(255), operator varchar(255), person_id bigint, primary key (id)) engine=InnoDB
alter table person add constraint UK_ohl6bjaenvpei97e3n955txgp unique (person_name)
alter table person add constraint FK7yj10mtpfmpopdalo5ohyabd1 foreign key (address_id) references address_entity (id)
alter table played_by add constraint FK97p0kuxl3wfekp8avtykh5yyt foreign key (persons_id) references person (id)
alter table played_by add constraint FKia721nx2sfsmd0tkhlq4scrs foreign key (games_id) references game_entity (id)
alter table telephone_number_entity add constraint FKjtqiwtsfhg80q6qdu1qvr24gg foreign key (person_id) references person (id)
