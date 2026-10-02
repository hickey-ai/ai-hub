export default {
  "cn": "项目执行",
  "en": "PROJECTOPS OS",
  "subtitle": "项目与里程碑跟踪",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "projects",
      "title": "项目档案",
      "singular": "项目档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "项目档案名称",
          "text",
          1
        ],
        [
          "owner",
          "负责人",
          "text",
          1
        ],
        [
          "location",
          "位置／备注",
          "text",
          1
        ]
      ],
      "columns": [
        "name",
        "owner",
        "location"
      ]
    },
    {
      "key": "milestones",
      "title": "项目里程碑",
      "singular": "项目里程碑",
      "icon": "◇",
      "fields": [
        [
          "projectId",
          "关联项目档案",
          "relation",
          1,
          "projects"
        ],
        [
          "deliverable",
          "交付物",
          "text",
          1
        ],
        [
          "eventDate",
          "登记日期",
          "date",
          1
        ],
        [
          "status",
          "状态",
          "select",
          1,
          [
            "待开始",
            "进行中",
            "已完成"
          ]
        ],
        [
          "notes",
          "备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "projectId",
        "deliverable",
        "eventDate",
        "status"
      ]
    }
  ]
}
