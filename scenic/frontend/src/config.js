export default {
  "cn": "景区管理",
  "en": "SCENIC OS",
  "subtitle": "景点资源与游览登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "spots",
      "title": "景点档案",
      "singular": "景点档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "景点档案名称",
          "text",
          1
        ],
        [
          "dailyCapacity",
          "参考日容量",
          "number",
          1
        ],
        [
          "location",
          "位置／备注",
          "text",
          1
        ],
        [
          "openingHours",
          "开放时间说明",
          "text",
          1
        ]
      ],
      "columns": [
        "name",
        "dailyCapacity",
        "location",
        "openingHours"
      ]
    },
    {
      "key": "visits",
      "title": "游览登记",
      "singular": "游览登记",
      "icon": "◇",
      "fields": [
        [
          "spotId",
          "关联景点档案",
          "relation",
          1,
          "spots"
        ],
        [
          "visitorGroup",
          "团队名称",
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
            "已预约",
            "已入园",
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
        "spotId",
        "visitorGroup",
        "eventDate",
        "status"
      ]
    }
  ]
}
