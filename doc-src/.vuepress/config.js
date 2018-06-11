module.exports = {
    title: 'HuiChe',
    description: '基于SpringBoot,QueryDsl 的 Java 快速开发框架',
    repo: 'https://github.com/jmjlbmn/huiche',
    themeConfig: {
        sidebarDepth: 3,
        repo: 'jmjlbmn/huiche',
        docsDir: 'doc-src',
        lastUpdated: 'Last Updated',
        editLinks: true,
        editLinkText: '帮我改善此页面',
        nav: [{
            text: '使用指南',
            link: '/guide/'
        },
            {
                text: '使用示例',
                link: 'https://github.com/jmjlbmn/huiche-examples/'
            },
            {
                text: 'JavaDoc',
                link: 'https://apidoc.gitee.com/jmjlbmn/huiche/'
            },
        ],
        sidebar: {
            '/guide/': [
                '',
                'data',
                'dao',
                'service',
                'web',
                'predicate',
                'search',
                'validate',
                'sql',
                'import'
            ]
        }
    }
};