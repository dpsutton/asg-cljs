(ns asg.styles.colors
  (:require [cljs-styled-components.reagent :as styled]
            [asg.styles.typography :as typo]))

(def acadiana-flag {:red    "#c00"
                    :blue   "navy"
                    :yellow "#fd0"})

(def primary (:blue acadiana-flag))
(def secondary (:red acadiana-flag))

(def stripes {:white  "#fff"
              :blue   primary
              :yellow (:yellow acadiana-flag)
              :red    (:red acadiana-flag)})

(def colors {:lightBlue "#43fff4"
             :blue "#0e1eb3"
             :lightYellow "#ffdc62"
             :yellow "#F1BA1F"
             :orange "#d55a14"
             :red "#b50f0f"
             :pink "#d04a4a"
             :white "#ffffff"
             :grey "rgba(0,0,0,.4)"})
