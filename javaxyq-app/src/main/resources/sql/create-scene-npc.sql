drop table if exists scene_npc;

create table scene_npc (
		id bigint  primary key,
		scene_id bigint not null,
		character_id varchar(20) not null,
		name varchar(30) not null,
		scene_x bigint not null,
		scene_y bigint not null,
		config varchar(2000) not null,
		description varchar(400)
	);

create index scene_npc_name_idx on scene_npc (name);

create index scene_npc_scene_id_idx on scene_npc (scene_id);

create unique index sql100418140421570 on scene_npc (id, scene_id);


