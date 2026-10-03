export default {
  "cn": "应急消防巡检",
  "en": "SAFETY LOG",
  "subtitle": "场所巡检与隐患处理手工台账",
  "accent": "#ba604a",
  "soft": "#fff1eb",
  "entities": [
    {
      "key": "sites",
      "title": "巡检场所",
      "singular": "巡检场所",
      "icon": "▣",
      "fields": [
        [
          "code",
          "场所编号",
          "text",
          1
        ],
        [
          "name",
          "场所名称",
          "text",
          1
        ],
        [
          "area",
          "所在区域",
          "text",
          1
        ],
        [
          "contact",
          "责任联系人",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "area",
        "contact"
      ]
    },
    {
      "key": "inspections",
      "title": "巡检记录",
      "singular": "巡检记录",
      "icon": "✓",
      "fields": [
        [
          "siteId",
          "关联场所",
          "relation",
          1,
          "sites"
        ],
        [
          "eventDate",
          "巡检日期",
          "date",
          1
        ],
        [
          "inspector",
          "巡检人",
          "text",
          1
        ],
        [
          "finding",
          "发现事项",
          "text",
          1
        ],
        [
          "status",
          "处理状态",
          "select",
          1,
          [
            "待处理",
            "处理中",
            "已复核"
          ]
        ],
        [
          "notes",
          "处理说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "siteId",
        "eventDate",
        "inspector",
        "finding",
        "status"
      ]
    }
  ]
}
