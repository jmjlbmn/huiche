/**
 * Welcome to your Workbox-powered service worker!
 *
 * You'll need to register this file in your web app and you should
 * disable HTTP caching for this file too.
 * See https://goo.gl/nhQhGp
 *
 * The rest of the code is auto-generated. Please don't update this file
 * directly; instead, make changes to your Workbox build configuration
 * and re-run your build process.
 * See https://goo.gl/2aRDsh
 */

importScripts("https://storage.googleapis.com/workbox-cdn/releases/3.2.0/workbox-sw.js");

/**
 * The workboxSW.precacheAndRoute() method efficiently caches and responds to
 * requests for URLs in the manifest.
 * See https://goo.gl/S9QRab
 */
self.__precacheManifest = [
    {
        "url": "404.html",
        "revision": "dd2a6b1ffafb6468a27c3528d5c36be7"
    },
    {
        "url": "assets/css/12.styles.d455e852.css",
        "revision": "0e5d7a5a0801a0b2375378ee5efcdc32"
    },
    {
        "url": "assets/img/search.83621669.svg",
        "revision": "83621669651b9a3d4bf64d1a670ad856"
    },
    {
        "url": "assets/js/0.7e58da3e.js",
        "revision": "42c758a698fc04457299b56c8a7fdfdf"
    },
    {
        "url": "assets/js/1.0f104f53.js",
        "revision": "54896d015ac2de9d711f65d67dd162a9"
    },
    {
        "url": "assets/js/10.dafc1230.js",
        "revision": "eed44ffdfb30db8acd469fd1fd61303f"
    },
    {
        "url": "assets/js/11.e3463ab6.js",
        "revision": "efc6eb08500d90e958f840aa22d43acd"
    },
    {
        "url": "assets/js/2.ea9280f3.js",
        "revision": "6ada18e7acd1eb28879e290d8487c199"
    },
    {
        "url": "assets/js/3.cebe33b8.js",
        "revision": "fce647e506be3c23db36a5781a1c9860"
    },
    {
        "url": "assets/js/4.252b1ab8.js",
        "revision": "124b3a9fdd08106f83ea1ac17af5e759"
    },
    {
        "url": "assets/js/5.89671cdd.js",
        "revision": "73f3b0f3c2854b637d6e4ec607f5a59e"
    },
    {
        "url": "assets/js/6.e1214ba4.js",
        "revision": "30d712d7b4344dfafe9f45d310e0e234"
    },
    {
        "url": "assets/js/7.c68657ce.js",
        "revision": "3d28deb535460e9c83381a35d1d2e011"
    },
    {
        "url": "assets/js/8.dbfbe5a0.js",
        "revision": "52d79638b3b5403db9c262b336a2b062"
    },
    {
        "url": "assets/js/9.2fe50fb0.js",
        "revision": "eaf2599132354c3fe64c3537bfefe0c9"
    },
    {
        "url": "assets/js/app.58b45464.js",
        "revision": "616e83ac75bc479b413a907550a10477"
    },
    {
        "url": "guide/assert.html",
        "revision": "9f75ff452e61ab40b37731fa53f8e7ba"
    },
    {
        "url": "guide/dao.html",
        "revision": "f4b95c96fc947c56c9d125a6bef89bd2"
    },
    {
        "url": "guide/data.html",
        "revision": "280b345a4f19462922de9411c317e13c"
    },
    {
        "url": "guide/import.html",
        "revision": "979c67c27035f9a26730a0bd70f3b31f"
    },
    {
        "url": "guide/index.html",
        "revision": "345179da00d2c230625445e548161e49"
    },
    {
        "url": "guide/predicate.html",
        "revision": "7b8201dd59a70c046fab2bb40884f1fc"
    },
    {
        "url": "guide/search.html",
        "revision": "001261a128e30260bb6e33b0d62c1cf5"
    },
    {
        "url": "guide/service.html",
        "revision": "458f636a1a4c1d54e0c868276a026f50"
    },
    {
        "url": "guide/sql.html",
        "revision": "d3178da7e1c17d532faa3597704baa5f"
    },
    {
        "url": "guide/validate.html",
        "revision": "20f76337922eccfbd2625ef7e54c3311"
    },
    {
        "url": "guide/web.html",
        "revision": "bb3fbccabec0c0983c0d4a013d793e95"
    },
    {
        "url": "huiche.png",
        "revision": "341ab9ef82bf3ab10192bd504b3148fa"
    },
    {
        "url": "index.html",
        "revision": "937d0499a63581b493e3b5dde74d6ed8"
    }
].concat(self.__precacheManifest || []);
workbox.precaching.suppressWarnings();
workbox.precaching.precacheAndRoute(self.__precacheManifest, {});
