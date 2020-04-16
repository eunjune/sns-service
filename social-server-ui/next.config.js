const withBunddleAnalyzer = require('@next/bundle-analyzer')({
    enabled: process.env.ANALYZE === 'true',
});
const webpack = require('webpack');
const CompressionPlugin = require('compression-webpack-plugin')

module.exports = withBunddleAnalyzer({
    distDir: '.next',

    webpack(config) {
        console.log('config',config);
        const prod = process.env.NODE_ENV === 'production';
        const plugins = [
            ...config.plugins,
            new webpack.ContextReplacementPlugin(/moment[/\\]locale$/, /^\.\/ko$/),
        ];

        if(prod) {
            plugins.push(new CompressionPlugin());
        }

        return {
            ...config,

            // 기본 설정에 덮어 씌울 부분
           mode: prod ? 'production' : 'development',
            devtool: prod ? 'hidden-source-map' : 'eval',
            module: {
                ...config.module,
                rules: [
                    ...config.module.rules,

                ],
            },
            plugins
        }
    }
});