export default {
  "cn": "质量管理",
  "en": "QMS OS",
  "subtitle": "标准与检查记录",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "standards",
      "title": "检验标准",
      "singular": "检验标准",
      "icon": "▣",
      "fields": [
        [
          "name",
          "检验标准名称",
          "text",
          1
        ],
        [
          "revision",
          "标准版本",
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
        "revision",
        "location"
      ]
    },
    {
      "key": "checks",
      "title": "质量检查",
      "singular": "质量检查",
      "icon": "◇",
      "fields": [
        [
          "standardId",
          "关联检验标准",
          "relation",
          1,
          "standards"
        ],
        [
          "batchCode",
          "批次编号",
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
            "待检",
            "检查中",
            "已记录"
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
        "standardId",
        "batchCode",
        "eventDate",
        "status"
      ]
    }
  ]
}
