export default {
  "cn": "演出赛事票务台账",
  "en": "EVENT DESK",
  "subtitle": "场馆与活动场次手工登记",
  "accent": "#8353c0",
  "soft": "#f5efff",
  "entities": [
    {
      "key": "venues",
      "title": "活动场馆",
      "singular": "活动场馆",
      "icon": "▣",
      "fields": [
        [
          "code",
          "场馆编号",
          "text",
          1
        ],
        [
          "name",
          "场馆名称",
          "text",
          1
        ],
        [
          "location",
          "场馆位置",
          "text",
          1
        ],
        [
          "capacity",
          "参考容量",
          "number",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "location",
        "capacity"
      ]
    },
    {
      "key": "shows",
      "title": "活动场次",
      "singular": "活动场次",
      "icon": "◇",
      "fields": [
        [
          "venueId",
          "关联场馆",
          "relation",
          1,
          "venues"
        ],
        [
          "title",
          "活动名称",
          "text",
          1
        ],
        [
          "eventDate",
          "活动日期",
          "date",
          1
        ],
        [
          "organizer",
          "主办方",
          "text",
          1
        ],
        [
          "status",
          "场次状态",
          "select",
          1,
          [
            "筹备中",
            "开放登记",
            "已结束",
            "已取消"
          ]
        ],
        [
          "notes",
          "场次说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "venueId",
        "title",
        "eventDate",
        "organizer",
        "status"
      ]
    }
  ]
}
