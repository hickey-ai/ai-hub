export default {
  "cn": "内容发布",
  "en": "CONTENT STUDIO",
  "subtitle": "从栏目到稿件，把编辑与发布状态整理成清楚的内容台账",
  "accent": "#7162c9",
  "soft": "#f0edff",
  "entities": [
    {
      "key": "sections",
      "title": "内容栏目",
      "singular": "内容栏目",
      "icon": "▦",
      "fields": [
        [
          "code",
          "栏目编码",
          "text",
          1
        ],
        [
          "name",
          "栏目名称",
          "text",
          1
        ],
        [
          "editor",
          "负责编辑",
          "text",
          1
        ],
        [
          "status",
          "栏目状态",
          "select",
          1,
          [
            "启用",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "editor",
        "status"
      ]
    },
    {
      "key": "articles",
      "title": "稿件记录",
      "singular": "稿件记录",
      "icon": "✦",
      "fields": [
        [
          "sectionId",
          "所属栏目",
          "relation",
          1,
          "sections"
        ],
        [
          "title",
          "稿件标题",
          "text",
          1
        ],
        [
          "author",
          "作者",
          "text",
          1
        ],
        [
          "publishDate",
          "计划发布日期",
          "date",
          1
        ],
        [
          "status",
          "稿件状态",
          "select",
          1,
          [
            "草稿",
            "待审核",
            "已发布",
            "已撤回"
          ]
        ],
        [
          "summary",
          "内容摘要",
          "textarea",
          0
        ]
      ],
      "columns": [
        "sectionId",
        "title",
        "author",
        "publishDate",
        "status"
      ]
    }
  ]
}
