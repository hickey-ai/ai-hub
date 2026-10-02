export default {
  "cn": "文化体育",
  "en": "VENUE FLOW",
  "subtitle": "场馆与活动预约，让空间使用安排清楚可见",
  "accent": "#b15b6c",
  "soft": "#fff0f2",
  "entities": [
    {
      "key": "venues",
      "title": "场馆空间",
      "singular": "场馆空间",
      "icon": "▦",
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
          "capacity",
          "容纳人数",
          "number",
          1
        ],
        [
          "type",
          "场馆类型",
          "select",
          1,
          [
            "展厅",
            "球馆",
            "排练室",
            "会议室"
          ]
        ],
        [
          "status",
          "使用状态",
          "select",
          1,
          [
            "开放",
            "维护",
            "暂停"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "capacity",
        "type",
        "status"
      ]
    },
    {
      "key": "events",
      "title": "活动安排",
      "singular": "活动安排",
      "icon": "✦",
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
          "organizer",
          "主办方",
          "text",
          1
        ],
        [
          "date",
          "活动日期",
          "date",
          1
        ],
        [
          "attendees",
          "预计人数",
          "number",
          1
        ],
        [
          "status",
          "活动状态",
          "select",
          1,
          [
            "筹备中",
            "已确认",
            "已完成",
            "已取消"
          ]
        ],
        [
          "notes",
          "活动备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "venueId",
        "title",
        "organizer",
        "date",
        "attendees",
        "status"
      ]
    }
  ]
}
