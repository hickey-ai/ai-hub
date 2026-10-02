export default {
  "cn": "建筑工程",
  "en": "BUILD GRID",
  "subtitle": "项目节点和施工日志，清楚呈现现场交付进度",
  "accent": "#b97830",
  "soft": "#fff2e7",
  "entities": [
    {
      "key": "projects",
      "title": "工程项目",
      "singular": "工程项目",
      "icon": "▣",
      "fields": [
        [
          "code",
          "项目编号",
          "text",
          1
        ],
        [
          "name",
          "项目名称",
          "text",
          1
        ],
        [
          "site",
          "施工地点",
          "text",
          1
        ],
        [
          "manager",
          "项目负责人",
          "text",
          1
        ],
        [
          "dueDate",
          "计划完工",
          "date",
          1
        ],
        [
          "status",
          "项目状态",
          "select",
          1,
          [
            "筹备",
            "施工中",
            "验收中",
            "已完成"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "site",
        "manager",
        "dueDate",
        "status"
      ]
    },
    {
      "key": "siteLogs",
      "title": "施工日志",
      "singular": "施工日志",
      "icon": "▤",
      "fields": [
        [
          "projectId",
          "关联项目",
          "relation",
          1,
          "projects"
        ],
        [
          "date",
          "记录日期",
          "date",
          1
        ],
        [
          "phase",
          "施工阶段",
          "text",
          1
        ],
        [
          "crew",
          "现场人数",
          "number",
          1
        ],
        [
          "risk",
          "安全风险",
          "select",
          1,
          [
            "无异常",
            "待整改",
            "已整改"
          ]
        ],
        [
          "notes",
          "现场记录",
          "textarea",
          0
        ]
      ],
      "columns": [
        "projectId",
        "date",
        "phase",
        "crew",
        "risk"
      ]
    }
  ]
}
