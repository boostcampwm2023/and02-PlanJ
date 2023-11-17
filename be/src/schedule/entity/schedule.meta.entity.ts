import {BaseEntity, Column, Entity, OneToMany, PrimaryColumn} from "typeorm";
import {ScheduleEntity} from "./schedule.entity";
import {ParticipantEntity} from "./participant.entity";

@Entity("ScheduleMeta")
export class ScheduleMetaEntity extends BaseEntity {
    @PrimaryColumn({name: "meta_id"})
    metaId: string;

    @Column({length:20})
    title: string;

    @Column({length:256})
    description: string;

    @Column({nullable:true, name:"start_time", type:"time", default: null})
    startTime: Date;

    @Column({name: "end_time", type:"time"})
    endTime: Date;

    /*
    * relation
     */
    @OneToMany(() => ScheduleEntity, (schedule) => schedule.scheduleId, {
        cascade: true,
    })
    children: ScheduleEntity[];

    @OneToMany(() => ParticipantEntity, (participant) => participant.meta, {
        cascade: true,
    })
    participant: ParticipantEntity[];
}