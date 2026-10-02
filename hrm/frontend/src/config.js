export default {
  "cn": "人力资源",
  "en": "PEOPLE DESK",
  "subtitle": "员工档案与假勤申请，面向小团队的清爽人事工作台",
  "accent": "#5767ad",
  "soft": "#edf1ff",
  "entities": [
    {
      "key": "employees",
      "title": "员工档案",
      "singular": "员工档案",
      "icon": "▦",
      "fields": [
        [
          "employeeNo",
          "员工编号",
          "text",
          1
        ],
        [
          "name",
          "姓名",
          "text",
          1
        ],
        [
          "department",
          "部门",
          "text",
          1
        ],
        [
          "role",
          "岗位",
          "text",
          1
        ],
        [
          "joinDate",
          "入职日期",
          "date",
          1
        ],
        [
          "status",
          "在职状态",
          "select",
          1,
          [
            "在职",
            "休假",
            "离职"
          ]
        ]
      ],
      "columns": [
        "employeeNo",
        "name",
        "department",
        "role",
        "joinDate",
        "status"
      ]
    },
    {
      "key": "leaveRequests",
      "title": "假勤申请",
      "singular": "假勤申请",
      "icon": "✦",
      "fields": [
        [
          "employeeId",
          "申请员工",
          "relation",
          1,
          "employees"
        ],
        [
          "startDate",
          "开始日期",
          "date",
          1
        ],
        [
          "endDate",
          "结束日期",
          "date",
          1
        ],
        [
          "type",
          "请假类型",
          "select",
          1,
          [
            "年假",
            "事假",
            "病假",
            "调休"
          ]
        ],
        [
          "status",
          "审批状态",
          "select",
          1,
          [
            "待审批",
            "已通过",
            "已驳回"
          ]
        ],
        [
          "notes",
          "申请说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "employeeId",
        "startDate",
        "endDate",
        "type",
        "status"
      ]
    }
  ]
}
