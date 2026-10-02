export default {
  "cn": "医美服务",
  "en": "AESTHETICS OS",
  "subtitle": "项目与咨询跟进",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "services",
      "title": "服务项目",
      "singular": "服务项目",
      "icon": "▣",
      "fields": [
        [
          "name",
          "服务项目名称",
          "text",
          1
        ],
        [
          "referencePrice",
          "参考价格",
          "money",
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
        "referencePrice",
        "location"
      ]
    },
    {
      "key": "consultations",
      "title": "咨询登记",
      "singular": "咨询登记",
      "icon": "◇",
      "fields": [
        [
          "serviceId",
          "关联服务项目",
          "relation",
          1,
          "services"
        ],
        [
          "visitorAlias",
          "访客代称",
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
            "待沟通",
            "沟通中",
            "已结束"
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
        "serviceId",
        "visitorAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
