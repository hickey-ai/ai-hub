export default {
  "cn": "药店台账",
  "en": "PHARMA DESK",
  "subtitle": "药品目录与批次记录，仅供本机学习演示",
  "accent": "#25998e",
  "soft": "#e7f7f4",
  "entities": [
    {
      "key": "medicines",
      "title": "药品目录",
      "singular": "药品目录",
      "icon": "▦",
      "fields": [
        [
          "code",
          "药品编码",
          "text",
          1
        ],
        [
          "name",
          "展示名称",
          "text",
          1
        ],
        [
          "form",
          "剂型",
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
          "status",
          "上架状态",
          "select",
          1,
          [
            "待核对",
            "上架",
            "下架"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "form",
        "unit",
        "status"
      ]
    },
    {
      "key": "batches",
      "title": "批次记录",
      "singular": "批次记录",
      "icon": "✦",
      "fields": [
        [
          "medicineId",
          "关联药品",
          "relation",
          1,
          "medicines"
        ],
        [
          "batchNo",
          "批次号",
          "text",
          1
        ],
        [
          "expiryDate",
          "标示效期",
          "date",
          1
        ],
        [
          "quantity",
          "记录数量",
          "number",
          1
        ],
        [
          "status",
          "批次状态",
          "select",
          1,
          [
            "待验收",
            "在库",
            "隔离",
            "已移出"
          ]
        ],
        [
          "notes",
          "记录备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "medicineId",
        "batchNo",
        "expiryDate",
        "quantity",
        "status"
      ]
    }
  ]
}
