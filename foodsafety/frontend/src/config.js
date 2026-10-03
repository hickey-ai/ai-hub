export default {
  "cn": "食品批次溯源",
  "en": "TRACE STUDIO",
  "subtitle": "批次与流转事件手工追踪",
  "accent": "#087d77",
  "soft": "#e4f7f3",
  "entities": [
    {
      "key": "batches",
      "title": "食品批次",
      "singular": "食品批次",
      "icon": "▦",
      "fields": [
        [
          "lotNo",
          "批次编号",
          "text",
          1
        ],
        [
          "product",
          "产品名称",
          "text",
          1
        ],
        [
          "source",
          "来源单位",
          "text",
          1
        ],
        [
          "productionDate",
          "生产日期",
          "date",
          1
        ],
        [
          "quantity",
          "批次数量",
          "number",
          1
        ]
      ],
      "columns": [
        "lotNo",
        "product",
        "source",
        "productionDate",
        "quantity"
      ]
    },
    {
      "key": "traceevents",
      "title": "流转记录",
      "singular": "流转记录",
      "icon": "↔",
      "fields": [
        [
          "batchId",
          "关联批次",
          "relation",
          1,
          "batches"
        ],
        [
          "checkpoint",
          "流转节点",
          "text",
          1
        ],
        [
          "eventDate",
          "记录日期",
          "date",
          1
        ],
        [
          "status",
          "处置状态",
          "select",
          1,
          [
            "正常",
            "待核查",
            "已隔离",
            "已召回"
          ]
        ],
        [
          "operator",
          "记录人",
          "text",
          1
        ],
        [
          "notes",
          "记录说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "batchId",
        "checkpoint",
        "eventDate",
        "status",
        "operator"
      ]
    }
  ]
}
