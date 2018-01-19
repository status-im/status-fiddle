(defproject status-fiddle "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [reagent "0.7.0" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [re-frame "0.10.2"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [re-frisk "0.5.3"]]
    :plugins      [[lein-figwheel "0.5.13"]]
    :cljsbuild    {:builds {:app {:figwheel {:on-jsload "status-fiddle.core/mount-root"}
                                  :compiler {:main                 status-fiddle.core
                                             :output-dir           "resources/public/js/compiled/out"
                                             :asset-path           "js/compiled/out"
                                             :source-map-timestamp true
                                             :preloads             [devtools.preload
                                                                    re-frisk.preload]
                                             :external-config      {:devtools/config {:features-to-install :all}}}}}}}
   :prod
   {:cljsbuild {:builds {:app {:compiler {:optimizations :simple
                                          :pretty-print  false}}}}}}

  :cljsbuild
  {:builds
   {:app {:source-paths ["src/cljs"]
          :compiler     {:main         status-fiddle.core
                         :output-to    "resources/public/js/compiled/app.js"
                         :foreign-libs [{:file     "resources/public/js/bundle.js"
                                         :provides ["cljsjs.react" "cljsjs.react.dom" "webpack.bundle"]}]}}}}

  :aliases {"figwheel-repl" ["with-profile" "dev" "figwheel"]
            "build-prod"    ["do"
                             ["clean"]
                             ["with-profile" "prod" "cljsbuild" "once"]]})
