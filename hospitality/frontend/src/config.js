export default {
  "cn": "酒店旅游",
  "en": "STAY STUDIO",
  "subtitle": "房态与预订一目了然，轻量管理每一次到店体验",
  "accent": "#a34a83",
  "soft": "#fdeef7",
  "entities": [
    {
      "key": "rooms",
      "title": "客房档案",
      "singular": "客房档案",
      "icon": "▦",
      "fields": [
        [
          "number",
          "房号",
          "text",
          1
        ],
        [
          "type",
          "房型",
          "select",
          1,
          [
            "标准间",
            "大床房",
            "套房"
          ]
        ],
        [
          "rate",
          "参考房价",
          "money",
          1
        ],
        [
          "status",
          "房态",
          "select",
          1,
          [
            "可预订",
            "维修中",
            "已停用"
          ]
        ]
      ],
      "columns": [
        "number",
        "type",
        "rate",
        "status"
      ]
    },
    {
      "key": "bookings",
      "title": "预订记录",
      "singular": "预订记录",
      "icon": "✦",
      "fields": [
        [
          "roomId",
          "关联客房",
          "relation",
          1,
          "rooms"
        ],
        [
          "guest",
          "客人称呼",
          "text",
          1
        ],
        [
          "checkIn",
          "入住日期",
          "date",
          1
        ],
        [
          "checkOut",
          "离店日期",
          "date",
          1
        ],
        [
          "status",
          "预订状态",
          "select",
          1,
          [
            "待确认",
            "已确认",
            "已入住",
            "已退房",
            "已取消"
          ]
        ],
        [
          "notes",
          "预订备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "roomId",
        "guest",
        "checkIn",
        "checkOut",
        "status"
      ]
    }
  ]
}
