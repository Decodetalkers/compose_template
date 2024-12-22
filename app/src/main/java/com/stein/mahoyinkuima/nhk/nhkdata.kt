package com.stein.mahoyinkuima.nhk

import kotlinx.serialization.*

@Serializable
data class NhkNews(
        val newsId: String,
        val title: String,
        val titleWithRuby: String,
        val outline: String,
        val outlineWithRuby: String,
        val body: String,
        val bodyWithoutHtml: String,
        val url: String,
        val m3u8Url: String,
        val imageUrl: String,
        val publishedAtUtc: String,
)

const val EMPTY_HTML =
        """
   <!DOCTYPE html>
   <html>
     <head>
       <meta name="viewport" content="width=device-width, initial-scale=1.0">
     </head>
     <body>
       <style>
         * {
           font-size: 16px;
         }
         h1 {
           font-size: 20px;
         }
         rt {
           font-size: 12px;
         }
         img {
           max-width: 100%;
         }
         .under {
           text-decoration-line: underline;
           -webkit-text-decoration-line: underline;
           text-decoration-color: #ff7f00;
           -webkit-text-decoration-color: #ff7f00;
         }
       </style>
     </body>
   </html>
   """

fun NhkNews.newsHtml(): String {
    var image = ""
    if (image == "") {
        image = "<img src=${imageUrl} />"
    }

    return """
          <!DOCTYPE html>
      <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body>
          <style>
            * {
              font-size: 16px;
            }
            h1 {
              font-size: 20px;
            }
            rt {
              font-size: 12px;
            }
            img {
              max-width: 100%;
            }
            .under {
              text-decoration-line: underline;
              -webkit-text-decoration-line: underline;
              text-decoration-color: #ff7f00;
              -webkit-text-decoration-color: #ff7f00;
            }
          </style>
          $image
          <h1>${titleWithRuby}</h1>
          ${body}
        </body>
      </html>
      """
}
