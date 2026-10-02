export default {
  "cn": "口腔门诊",
  "en": "DENTAL OS",
  "subtitle": "牙椅与就诊排期",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "chairs",
      "title": "牙椅档案",
      "singular": "牙椅档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "牙椅档案名称",
          "text",
          1
        ],
        [
          "room",
          "诊室编号",
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
        "room",
        "location"
      ]
    },
    {
      "key": "visits",
      "title": "就诊登记",
      "singular": "就诊登记",
      "icon": "◇",
      "fields": [
        [
          "chairId",
          "关联牙椅档案",
          "relation",
          1,
          "chairs"
        ],
        [
          "patientAlias",
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
            "预约中",
            "已到诊",
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
        "chairId",
        "patientAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
