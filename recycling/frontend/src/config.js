export default {
  "cn": "再生资源回收",
  "en": "RECYCLE DESK",
  "subtitle": "物料、称重与回收结算记录",
  "accent": "#218077",
  "soft": "#e8f7f2",
  "entities": [
    {
      "key": "materials",
      "title": "回收物料",
      "singular": "回收物料",
      "icon": "♻",
      "fields": [
        [
          "code",
          "物料编号",
          "text",
          1
        ],
        [
          "name",
          "物料名称",
          "text",
          1
        ],
        [
          "unit",
          "计量单位",
          "text",
          1
        ],
        [
          "price",
          "参考单价",
          "money",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "unit",
        "price"
      ]
    },
    {
      "key": "receipts",
      "title": "回收单据",
      "singular": "回收单据",
      "icon": "▣",
      "fields": [
        [
          "materialId",
          "关联物料",
          "relation",
          1,
          "materials"
        ],
        [
          "receiptNo",
          "单据编号",
          "text",
          1
        ],
        [
          "source",
          "来源代称",
          "text",
          1
        ],
        [
          "weight",
          "重量（千克）",
          "money",
          1
        ],
        [
          "amount",
          "应付金额",
          "money",
          1
        ],
        [
          "eventDate",
          "回收日期",
          "date",
          1
        ],
        [
          "status",
          "单据状态",
          "select",
          1,
          [
            "待核对",
            "已结算",
            "已作废"
          ]
        ],
        [
          "notes",
          "分类与流转备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "materialId",
        "receiptNo",
        "source",
        "weight",
        "amount",
        "eventDate",
        "status"
      ]
    }
  ]
}
