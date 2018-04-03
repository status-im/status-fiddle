(defproject status-fiddle "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [alandipert/storage-atom "2.0.1"]
                 [cljs-ajax "0.5.8"]
                 [com.cemerick/url "0.1.1"]
                 [reagent "0.7.0" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [hickory "0.7.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [re-frame "0.10.2"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["components/src" "src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [re-frisk "0.5.3"]]
    :plugins      [[lein-figwheel "0.5.13"]]
    :cljsbuild    {:builds {:app {:source-paths ["components/src" "src/cljs" "src/dev"]
                                  :figwheel {:on-jsload "status-fiddle.core/mount-root"}
                                  :compiler {:main                 status-fiddle.core
                                             :output-dir           "resources/public/js/compiled/out"
                                             :asset-path           "js/compiled/out"
                                             :source-map-timestamp true
                                             :preloads             [devtools.preload
                                                                    re-frisk.preload]
                                             :external-config      {:devtools/config {:features-to-install :all}}}}}}}
   :prod
   {:cljsbuild {:builds {:app {:source-paths ["components/src" "src/cljs" "src/prod"]
                               :compiler {:optimizations   :whitespace
                                          :pretty-print    false}}}}}}
  :cljsbuild
  {:builds
   {:app {:compiler     {:main         status-fiddle.core
                         :output-to    "resources/public/js/compiled/app.js"
                         :foreign-libs [{:file     "resources/public/js/bundle.js"
                                         :provides ["cljsjs.react" "cljsjs.react.dom" "webpack.bundle"]}]}}}}

  :aliases {"figwheel-repl" ["with-profile" "dev" "figwheel"]
            "build-prod"    ["do"
                             ["clean"]
                             ["with-profile" "prod" "cljsbuild" "once"]]})
