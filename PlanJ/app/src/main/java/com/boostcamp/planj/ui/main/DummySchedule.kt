package com.boostcamp.planj.ui.main

import com.boostcamp.planj.data.model.Repetition
import com.boostcamp.planj.data.model.Schedule
import com.boostcamp.planj.data.model.User

object DummySchedule {

        private val scheduleList: List<Schedule> = listOf(
            Schedule(
                scheduleId = "1",
                title = "일정1",
                memo = null,
                startTime = "2023-11-14T09:00:00.00",
                endTime = "2023-11-15T18:00:00.00",
                repeat = null,
                members = listOf(
                    User("1")
                ),
                doneMembers = null,
                location = null,
                categoryTitle = "미분류",
                finished = false,
                failed = false
            ),

            Schedule(
                scheduleId = "2",
                title = "일정2",
                memo = null,
                startTime = null,
                endTime = "2023-11-15T17:00:00.00",
                repeat = null,
                members = listOf(
                    User("1"),
                    User("2")
                ),
                doneMembers = listOf(
                    User("2")
                ),
                location = null,
                categoryTitle = "카테고리1",
                finished = false,
                failed = true
            ),

            Schedule(
                scheduleId = "3",
                title = "일정3",
                memo = null,
                startTime = null,
                endTime = "2023-11-15T18:00:00.00",
                repeat = Repetition(
                    "1",
                    "매주",
                    0,
                    1
                ),
                members = listOf(
                    User("1"),
                    User("3"),
                    User("4")
                ),
                doneMembers = listOf(
                    User("1"),
                    User("4")
                ),
                location = null,
                categoryTitle = "카테고리1",
                finished = true,
                failed = false
            ),

            Schedule(
                scheduleId = "4",
                title = "일정4",
                memo = null,
                startTime = "2023-11-13T12:00:00.00",
                endTime = "2023-11-15T18:00:00.00",
                repeat = null,
                members = listOf(
                    User("1")
                ),
                doneMembers = listOf(
                    User("1")
                ),
                location = "서울 구리시 ...",
                categoryTitle = "카테고리2",
                finished = true,
                failed = true
            ),

            Schedule(
                scheduleId = "5",
                title = "일정5",
                memo = null,
                startTime = null,
                endTime = "2023-11-15T12:00:00.00",
                repeat = Repetition(
                    "1",
                    "매일",
                    2,
                    0
                ),
                members = listOf(
                    User("1")
                ),
                doneMembers = null,
                location = "서울 성동구 왕십리",
                categoryTitle = "카테고리3",
                finished = false,
                failed = false
            ),

            Schedule(
                scheduleId = "6",
                title = "일정6",
                memo = null,
                startTime = null,
                endTime = "2023-11-15T11:00:00.00",
                repeat = null,
                members = listOf(
                    User("1")
                ),
                doneMembers = null,
                location = null,
                categoryTitle = "미분류",
                finished = false,
                failed = true
            ),

            )


        fun getDummyList() : List<Schedule> = scheduleList


}