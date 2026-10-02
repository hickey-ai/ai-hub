export default {
  "cn": "园区运营",
  "en": "PARKOPS OS",
  "subtitle": "园区设施与巡检台账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "parks",
      "title": "园区档案",
      "singular": "园区档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "园区档案名称",
          "text",
          1
        ],
        [
          "district",
          "所在区域",
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
        "district",
        "location"
      ]
    },
    {
      "key": "inspections",
      "title": "巡检记录",
      "singular": "巡检记录",
      "icon": "◇",
      "fields": [
        [
          "parkId",
          "关联园区档案",
          "relation",
          1,
          "parks"
        ],
        [
          "inspector",
          "巡检人员",
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
            "待处理",
            "处理中",
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
        "parkId",
        "inspector",
        "eventDate",
        "status"
      ]
    }
  ]
}
