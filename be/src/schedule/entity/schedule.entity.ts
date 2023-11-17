import {BaseEntity, Column, Entity, ManyToOne, PrimaryColumn} from "typeorm";
import {ScheduleMetaEntity} from "./schedule.meta.entity";

@Entity("Schedule")
export class ScheduleEntity extends BaseEntity {
    @PrimaryColumn({name:"schedule_id"})
    scheduleId: string;

    @Column()
    startAt: Date;

    @Column()
    endAt: Date;

    @Column({default: false})
    finished: boolean;

    @Column({default: false})
    failed: boolean;

    @Column({length:256, name: "remind_memo", default:null, nullable:true})
    remindMemo: string;

    @Column({default: true})
    last: boolean;

    /*
    * relation
     */
    @ManyToOne(() => ScheduleMetaEntity, (parent) => parent.metaId, {
        onDelete: 'CASCADE',
    })
    parent?: ScheduleMetaEntity;
}