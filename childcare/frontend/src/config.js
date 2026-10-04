export default {
  "cn": "托育机构",
  "en": "CHILDCARE DESK",
  "subtitle": "班级、接送与日常照护交接",
  "accent": "#8066ac",
  "soft": "#f4effa",
  "entities": [
    {
      "key": "groups",
      "title": "托育班级",
      "singular": "托育班级",
      "icon": "▣",
      "fields": [
        [
          "code",
          "班级编号",
          "text",
          1
        ],
        [
          "name",
          "班级名称",
          "text",
          1
        ],
        [
          "room",
          "活动室",
          "text",
          1
        ],
        [
          "staff",
          "责任老师代称",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "room",
        "staff"
      ]
    },
    {
      "key": "handoffs",
      "title": "照护交接",
      "singular": "照护交接",
      "icon": "♡",
      "fields": [
        [
          "groupId",
          "所属班级",
          "relation",
          1,
          "groups"
        ],
        [
          "childAlias",
          "儿童代称",
          "text",
          1
        ],
        [
          "guardianAlias",
          "接送人代称",
          "text",
          1
        ],
        [
          "eventDate",
          "交接日期",
          "date",
          1
        ],
        [
          "type",
          "交接类型",
          "select",
          1,
          [
            "入园",
            "离园"
          ]
        ],
        [
          "status",
          "交接状态",
          "select",
          1,
          [
            "待交接",
            "已交接",
            "异常待核"
          ]
        ],
        [
          "notes",
          "喂养午睡与交接备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "groupId",
        "childAlias",
        "guardianAlias",
        "eventDate",
        "type",
        "status"
      ]
    }
  ]
}
