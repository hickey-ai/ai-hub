export default {
  "cn": "检验台账",
  "en": "LIS OS",
  "subtitle": "项目与样本流转演示",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "assays",
      "title": "检验项目",
      "singular": "检验项目",
      "icon": "▣",
      "fields": [
        [
          "name",
          "检验项目名称",
          "text",
          1
        ],
        [
          "specimenType",
          "标本类型",
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
        "specimenType",
        "location"
      ]
    },
    {
      "key": "samples",
      "title": "样本登记",
      "singular": "样本登记",
      "icon": "◇",
      "fields": [
        [
          "assayId",
          "关联检验项目",
          "relation",
          1,
          "assays"
        ],
        [
          "sampleCode",
          "样本编号",
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
            "待接收",
            "处理中",
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
        "assayId",
        "sampleCode",
        "eventDate",
        "status"
      ]
    }
  ]
}
