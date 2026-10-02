export default {
  "cn": "制造执行",
  "en": "FACTORY OS",
  "subtitle": "从物料到工单，给生产现场一个清楚的进度视图",
  "accent": "#744fca",
  "soft": "#f3edff",
  "entities": [
    {
      "key": "materials",
      "title": "物料档案",
      "singular": "物料档案",
      "icon": "▦",
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
          "单位",
          "text",
          1
        ],
        [
          "available",
          "可用数量",
          "number",
          1
        ],
        [
          "supplier",
          "供应商",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "unit",
        "available",
        "supplier"
      ]
    },
    {
      "key": "workorders",
      "title": "生产工单",
      "singular": "生产工单",
      "icon": "◈",
      "fields": [
        [
          "materialId",
          "主要物料",
          "relation",
          1,
          "materials"
        ],
        [
          "orderNo",
          "工单编号",
          "text",
          1
        ],
        [
          "product",
          "生产产品",
          "text",
          1
        ],
        [
          "quantity",
          "计划数量",
          "number",
          1
        ],
        [
          "dueDate",
          "交付日期",
          "date",
          1
        ],
        [
          "status",
          "工单状态",
          "select",
          1,
          [
            "待排产",
            "生产中",
            "质检中",
            "已完成"
          ]
        ],
        [
          "notes",
          "生产备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "materialId",
        "orderNo",
        "product",
        "quantity",
        "dueDate",
        "status"
      ]
    }
  ]
}
