export default {
  "cn": "会员运营",
  "en": "LOYALTY OS",
  "subtitle": "等级与会员登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "tiers",
      "title": "会员等级",
      "singular": "会员等级",
      "icon": "▣",
      "fields": [
        [
          "name",
          "会员等级名称",
          "text",
          1
        ],
        [
          "pointsThreshold",
          "积分门槛",
          "number",
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
        "pointsThreshold",
        "location"
      ]
    },
    {
      "key": "members",
      "title": "会员档案",
      "singular": "会员档案",
      "icon": "◇",
      "fields": [
        [
          "tierId",
          "关联会员等级",
          "relation",
          1,
          "tiers"
        ],
        [
          "memberAlias",
          "会员代称",
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
            "新建",
            "活跃",
            "暂停"
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
        "tierId",
        "memberAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
