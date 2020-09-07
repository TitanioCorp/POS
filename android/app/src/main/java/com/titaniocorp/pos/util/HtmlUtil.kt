package com.titaniocorp.pos.util

import com.titaniocorp.pos.app.model.Billing
import java.util.*

object HtmlUtil {
    fun getBilling(billing: Billing): String = "<html>" +
            "  <head>" +
            "    <style>" +
            "      /* Resset CSS */" +
            "      html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn, em, img, ins, kbd, q, s, samp," +
            "      small, strike, strong, sub, sup, tt, var, b, u, i, center, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article," +
            "      aside, canvas, details, embed, figure, figcaption, footer, header, hgroup, menu, nav, output, ruby, section, summary, time, mark, audio, video {" +
            "      margin: 0;" +
            "      padding: 0;" +
            "      border: 0;" +
            "      font-size: 100%;" +
            "      font: inherit;" +
            "      vertical-align: baseline;" +
            "      }" +
            "      article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section { display: block; }" +
            "      body { line-height: 1; }" +
            "      ol, ul { list-style: none; }" +
            "      blockquote, q { quotes: none; }" +
            "      blockquote:before, blockquote:after, q:before, q:after {" +
            "      content: '';" +
            "      content: none;" +
            "      }" +
            "      table {" +
            "      border-collapse: collapse;" +
            "      border-spacing: 0;" +
            "      }" +
            "" +
            "      * { box-sizing: border-box; }" +
            "" +
            "      .clear{ clear: both; }" +
            "" +
            "      /* End resset CSS */" +
            "      .text-primary{ color: #008577; }" +
            "" +
            "      .text-aling-center{ text-align: center; }" +
            "" +
            "      .text-bold{ font-weight: bold !important; }" +
            "" +
            "      .wrapper-content{" +
            "        width: 50%;" +
            "        margin: auto;" +
            "        font-family: Arial, Helvetica, sans-serif;" +
            "        padding: 16px;" +
            "      }" +
            "" +
            "      .content{" +
            "        width: 100%;" +
            "        border-radius: 24px;" +
            "        border: 1px solid #CCC;" +
            "        padding: 16px;" +
            "      }" +
            "" +
            "      .title{ font-size: 24px; }" +
            "" +
            "      .title-date{" +
            "        font-size: 22px;" +
            "        margin: 8px 0px 16px 0px;" +
            "      }" +
            "" +
            "      .item{ padding: 12px 0px; }" +
            "" +
            "      .item-section{" +
            "        padding: 16px 0px  0px 0px;" +
            "        font-weight: bold;" +
            "      }" +
            "" +
            "      .item-row{ border-bottom: 1px solid #CCC; }" +
            "" +
            "      .label{" +
            "        width: 50%;" +
            "        text-align: left;" +
            "        float:left;" +
            "        text-transform: uppercase;" +
            "        font-size: 12px;" +
            "      }" +
            "" +
            "      .value{" +
            "        width: 50%;" +
            "        text-align: right;" +
            "        float:left;" +
            "      }" +
            "" +
            "      .footer{" +
            "        width: 100%;" +
            "        padding: 16px 24px 0px 24px;" +
            "        font-size: 14px;" +
            "      }" +
            "" +
            "      .footer .developed{ float:left; }" +
            "      .footer .year{ float:right; }" +
            "" +
            "      @media screen and (max-width: 1000px) {" +
            "        .wrapper-content{" +
            "          width: 70%;" +
            "          padding: 8px;" +
            "        }" +
            "      }" +
            "" +
            "      @media screen and (max-width: 800px) {" +
            "        .wrapper-content{" +
            "          width: 100%;" +
            "          padding: 8px;" +
            "        }" +
            "      }" +
            "    </style>" +
            "  </head>" +
            "  <body>" +
            "    <div class=\"wrapper-content\">" +
            "      <h2 class=\"text-aling-center title\">Cierre de caja</h2>" +
            "      <h2 class=\"text-primary text-aling-center title-date\">${Date().toFormatString()}</h2>" +
            "" +
            "      <div class=\"content\">" +
            "        <ul>" +
            "          <li class=\"item item-section\">" +
            "            Detalle de ventas" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Costo</div>" +
            "            <div class=\"value\">\$${billing.cost.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Ganancia</div>" +
            "            <div class=\"value\">\$${billing.profit.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Reembolso</div>" +
            "            <div class=\"value\">\$${billing.refund.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Abonos</div>" +
            "            <div class=\"value\">\$${billing.payments.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Ajuste</div>" +
            "            <div class=\"value\">\$${billing.adjustment.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item\">" +
            "            <div class=\"label\">Impuesto</div>" +
            "            <div class=\"value\">\$${billing.tax.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-section\">" +
            "            Otros" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Compra de inventario</div>" +
            "            <div class=\"value\">\$${billing.stock.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Otros egresos</div>" +
            "            <div class=\"value\">\$${billing.expenses.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item\">" +
            "            <div class=\"label\">Otros ingresos</div>" +
            "            <div class=\"value\">\$${billing.income.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-section\">" +
            "            Total" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Total ventas</div>" +
            "            <div class=\"value\">\$${billing.totalPurchase.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Total egresos</div>" +
            "            <div class=\"value\">\$${billing.totalExpenses.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Total ingresos</div>" +
            "            <div class=\"value\">\$${billing.totalIncome.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item\">" +
            "            <div class=\"label text-primary text-bold\">Total</div>" +
            "            <div class=\"value text-primary text-bold\">\$${billing.total.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "        </ul>" +
            "      </div>" +
            "" +
            "      <div class=\"footer\">" +
            "        <div class=\"developed\">" +
            "          Developed by <b class=\"text-primary\">TitanioCorp</b>" +
            "        </div>" +
            "        <div class=\"year\">" +
            "          2020" +
            "        </div>" +
            "        <div class=\"clear\">" +
            "        </div>" +
            "      </div>" +
            "    </div>" +
            "  </body>" +
            "</html>"

    fun getReport(
        startDate: Date,
        endDate: Date,
        billing: Billing
    ): String = "<html>" +
            "  <head>" +
            "    <style>" +
            "      /* Resset CSS */" +
            "      html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn, em, img, ins, kbd, q, s, samp," +
            "      small, strike, strong, sub, sup, tt, var, b, u, i, center, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article," +
            "      aside, canvas, details, embed, figure, figcaption, footer, header, hgroup, menu, nav, output, ruby, section, summary, time, mark, audio, video {" +
            "      margin: 0;" +
            "      padding: 0;" +
            "      border: 0;" +
            "      font-size: 100%;" +
            "      font: inherit;" +
            "      vertical-align: baseline;" +
            "      }" +
            "      article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section { display: block; }" +
            "      body { line-height: 1; }" +
            "      ol, ul { list-style: none; }" +
            "      blockquote, q { quotes: none; }" +
            "      blockquote:before, blockquote:after, q:before, q:after {" +
            "      content: '';" +
            "      content: none;" +
            "      }" +
            "      table {" +
            "      border-collapse: collapse;" +
            "      border-spacing: 0;" +
            "      }" +
            "" +
            "      * { box-sizing: border-box; }" +
            "" +
            "      .clear{ clear: both; }" +
            "" +
            "      /* End resset CSS */" +
            "      .text-primary{ color: #008577; }" +
            "" +
            "      .text-aling-center{ text-align: center; }" +
            "" +
            "      .text-bold{ font-weight: bold !important; }" +
            "" +
            "      .wrapper-content{" +
            "        width: 50%;" +
            "        margin: auto;" +
            "        font-family: Arial, Helvetica, sans-serif;" +
            "        padding: 16px;" +
            "      }" +
            "" +
            "      .content{" +
            "        width: 100%;" +
            "        border-radius: 24px;" +
            "        border: 1px solid #CCC;" +
            "        padding: 16px;" +
            "      }" +
            "" +
            "      .title{ font-size: 24px; }" +
            "" +
            "      .title-date{" +
            "        font-size: 22px;" +
            "        margin: 8px 0px 16px 0px;" +
            "      }" +
            "" +
            "      .item{ padding: 12px 0px; }" +
            "" +
            "      .item-section{" +
            "        padding: 16px 0px  0px 0px;" +
            "        font-weight: bold;" +
            "      }" +
            "" +
            "      .item-row{ border-bottom: 1px solid #CCC; }" +
            "" +
            "      .label{" +
            "        width: 50%;" +
            "        text-align: left;" +
            "        float:left;" +
            "        text-transform: uppercase;" +
            "        font-size: 12px;" +
            "      }" +
            "" +
            "      .value{" +
            "        width: 50%;" +
            "        text-align: right;" +
            "        float:left;" +
            "      }" +
            "" +
            "      .footer{" +
            "        width: 100%;" +
            "        padding: 16px 24px 0px 24px;" +
            "        font-size: 14px;" +
            "      }" +
            "" +
            "      .footer .developed{ float:left; }" +
            "      .footer .year{ float:right; }" +
            "" +
            "      @media screen and (max-width: 1000px) {" +
            "        .wrapper-content{" +
            "          width: 70%;" +
            "          padding: 8px;" +
            "        }" +
            "      }" +
            "" +
            "      @media screen and (max-width: 800px) {" +
            "        .wrapper-content{" +
            "          width: 100%;" +
            "          padding: 8px;" +
            "        }" +
            "      }" +
            "    </style>" +
            "  </head>" +
            "  <body>" +
            "    <div class=\"wrapper-content\">" +
            "      <h2 class=\"text-aling-center title\">Reporte</h2>" +
            "      <h2 class=\"text-primary text-aling-center title-date\">${startDate.toFormatString()} - ${endDate.toFormatString()}</h2>" +
            "" +
            "      <div class=\"content\">" +
            "        <ul>" +
            "          <li class=\"item item-section\">" +
            "            Detalle de ventas" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Costo</div>" +
            "            <div class=\"value\">\$${billing.cost.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Ganancia</div>" +
            "            <div class=\"value\">\$${billing.profit.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Reembolso</div>" +
            "            <div class=\"value\">\$${billing.refund.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Abonos</div>" +
            "            <div class=\"value\">\$${billing.payments.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Ajuste</div>" +
            "            <div class=\"value\">\$${billing.adjustment.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item\">" +
            "            <div class=\"label\">Impuesto</div>" +
            "            <div class=\"value\">\$${billing.tax.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-section\">" +
            "            Otros" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Compra de inventario</div>" +
            "            <div class=\"value\">\$${billing.stock.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Otros egresos</div>" +
            "            <div class=\"value\">\$${billing.expenses.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item\">" +
            "            <div class=\"label\">Otros ingresos</div>" +
            "            <div class=\"value\">\$${billing.income.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-section\">" +
            "            Total" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Total ventas</div>" +
            "            <div class=\"value\">\$${billing.totalPurchase.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Total egresos</div>" +
            "            <div class=\"value\">\$${billing.totalExpenses.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item item-row\">" +
            "            <div class=\"label\">Total ingresos</div>" +
            "            <div class=\"value\">\$${billing.totalIncome.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "" +
            "          <li class=\"item\">" +
            "            <div class=\"label text-primary text-bold\">Total</div>" +
            "            <div class=\"value text-primary text-bold\">\$${billing.total.asMoney()}</div>" +
            "            <div class=\"clear\"></div>" +
            "          </li>" +
            "        </ul>" +
            "      </div>" +
            "" +
            "      <div class=\"footer\">" +
            "        <div class=\"developed\">" +
            "          Developed by <b class=\"text-primary\">TitanioCorp</b>" +
            "        </div>" +
            "        <div class=\"year\">" +
            "          2020" +
            "        </div>" +
            "        <div class=\"clear\">" +
            "        </div>" +
            "      </div>" +
            "    </div>" +
            "  </body>" +
            "</html>"
}