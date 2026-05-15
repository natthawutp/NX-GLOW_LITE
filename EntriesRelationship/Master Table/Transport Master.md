# GWH_TM_TRSP
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | TRSP_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | TRSP_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | TRSP_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | TRSP_COD | NVARCHAR2(10) | N | Transport Type Code / 輸送モード |
| 22 | TRSP_NAM | NVARCHAR2(100) | N | Transport Type Name / 輸送モード名称 |
| 23 | TRSP_CAR_COD | NVARCHAR2(12) | Y | Transport Career Code / 会社コード |
| 24 | TRSP_TYPE_KND | CHAR(2) | Y | Transport Type Kind / 便区分 |
| 25 | TRSP_DLVR_NOTE_KND | NVARCHAR2(10) | Y | Delivery Note Kind / 送状種類 |
| 26 | TRSP_GET_POST_KND | CHAR(1) | N | Get Post Kind / GET-POST区分 |
| 27 | TRSP_URL | NVARCHAR2(200) | Y | URL For Trace Search / URL |
| 28 | TRSP_PWIN_FLG | CHAR(1) | N | Packing Weight Input Flag / 梱包時重量入力フラグ |
| 29 | TRSP_VW_KND | CHAR(1) | Y | Volume Weight Colciration Kind / 容積重量計算種別区分 |
| 30 | TRSP_OKPT_COD | CHAR(1) | N | Okinawa Arrival and Departure Port Code / 沖縄発着出航港コード |
| 31 | TRSP_SZWT_KND | NVARCHAR2(10) | Y | Size Weight Kind / サイズ重量区分 |
| 32 | TRSP_PDIN_FLG | CHAR(1) | N | Packing Delivery Input Flag / 梱包時配送入力＆送り状発行フラグ |
| 33 | TRSP_SALS_STR_COD | CHAR(10) | Y | Sales Store Code / 営業店コード |
| 34 | TRSP_DLVR_NOTE_PROD_NAM | NVARCHAR2(50) | Y | Delivery Note Product Name / 送り状印刷用商品名 |
| 35 | TRSP_ID_COD4 | NVARCHAR2(30) | Y | Identification Code 4 / 識別コード4 |
| 36 | TRSP_HPDN_FLG | CHAR(1) | N | (HHT)Packing Delivery Note No Input Flag / (HHT)梱包時送り状入力フラグ |
| 37 | TRSP_ORDC_KND | CHAR(1) | Y | Original document Kind / 原票区分 |
| 38 | TRSP_PPCC_KND | CHAR(1) | Y | Prepaid Collect Kind / 元着区分 |
| 39 | TRSP_FRT_KND | CHAR(1) | Y | Freight Kind / 運賃区分 |
| 40 | TRSP_DLV_INST_KND | CHAR(1) | Y | Delivery Instruction Kind / 配達指示区分 |
| 41 | TRSP_TRC_SP_COD | NVARCHAR2(16) | Y | Trace Shipper Code / 発トレース用荷主コード |
| 42 | TRSP_SP_JIS_COD | NVARCHAR2(22) | Y | Shipper JIS Code / 発荷主市町村JISコード |
| 43 | TRSP_HNST_COD | NVARCHAR2(8) | Y | Handling Store Code / 取扱店コード |
| 44 | TRSP_BILL_COD1 | NVARCHAR2(40) | Y | Billing Code1 / 運賃請求先コード1(1個口) |
| 45 | TRSP_BILL_COD2 | NVARCHAR2(40) | Y | Billing Code2 / 運賃請求先コード2(複数個口) |
| 46 | TRSP_PRCS_COD | NVARCHAR2(20) | Y | (US) Precision code / (US) プレシジョンコード |
| 47 | TRSP_STI1 | NVARCHAR2(100) | Y | Reserve Item 01 / 予備項目01 |
| 48 | TRSP_STI2 | NVARCHAR2(100) | Y | Reserve Item 02 / 予備項目02 |
| 49 | TRSP_STI3 | NVARCHAR2(100) | Y | Reserve Item 03 / 予備項目03 |
| 50 | TRSP_STI4 | NVARCHAR2(100) | Y | Reserve Item 04 / 予備項目04 |
| 51 | TRSP_STI5 | NVARCHAR2(100) | Y | Reserve Item 05 / 予備項目05 |
| 52 | TRSP_STI6 | NVARCHAR2(100) | Y | Reserve Item 06 / 予備項目06 |
| 53 | TRSP_STI7 | NVARCHAR2(100) | Y | Reserve Item 07 / 予備項目07 |
| 54 | TRSP_STI8 | NVARCHAR2(100) | Y | Reserve Item 08 / 予備項目08 |
| 55 | TRSP_STI9 | NVARCHAR2(100) | Y | Reserve Item 09 / 予備項目09 |
| 56 | TRSP_STI10 | NVARCHAR2(100) | Y | Reserve Item 10 / 予備項目10 |
| 57 | TRSP_STI11 | NVARCHAR2(100) | Y | Reserve Item 11 / 予備項目11 |
| 58 | TRSP_STI12 | NVARCHAR2(100) | Y | Reserve Item 12 / 予備項目12 |
| 59 | TRSP_STI13 | NVARCHAR2(100) | Y | Reserve Item 13 / 予備項目13 |
| 60 | TRSP_STI14 | NVARCHAR2(100) | Y | Reserve Item 14 / 予備項目14 |
| 61 | TRSP_STI15 | NVARCHAR2(100) | Y | Reserve Item 15 / 予備項目15 |
| 62 | TRSP_CIT1 | NVARCHAR2(100) | Y | Customized Item 01 / 顧客専用項目01 |
| 63 | TRSP_CIT2 | NVARCHAR2(100) | Y | Customized Item 02 / 顧客専用項目02 |
| 64 | TRSP_CIT3 | NVARCHAR2(100) | Y | Customized Item 03 / 顧客専用項目03 |
| 65 | TRSP_CIT4 | NVARCHAR2(100) | Y | Customized Item 04 / 顧客専用項目04 |
| 66 | TRSP_CIT5 | NVARCHAR2(100) | Y | Customized Item 05 / 顧客専用項目05 |
| 67 | TRSP_CIT6 | NVARCHAR2(100) | Y | Customized Item 06 / 顧客専用項目06 |
| 68 | TRSP_CIT7 | NVARCHAR2(100) | Y | Customized Item 07 / 顧客専用項目07 |
| 69 | TRSP_CIT8 | NVARCHAR2(100) | Y | Customized Item 08 / 顧客専用項目08 |
| 70 | TRSP_CIT9 | NVARCHAR2(100) | Y | Customized Item 09 / 顧客専用項目09 |
| 71 | TRSP_CIT10 | NVARCHAR2(100) | Y | Customized Item 10 / 顧客専用項目10 |
| 72 | TRSP_CIT11 | NVARCHAR2(100) | Y | Customized Item 11 / 顧客専用項目11 |
| 73 | TRSP_CIT12 | NVARCHAR2(100) | Y | Customized Item 12 / 顧客専用項目12 |
| 74 | TRSP_CIT13 | NVARCHAR2(100) | Y | Customized Item 13 / 顧客専用項目13 |
| 75 | TRSP_CIT14 | NVARCHAR2(100) | Y | Customized Item 14 / 顧客専用項目14 |
| 76 | TRSP_CIT15 | NVARCHAR2(100) | Y | Customized Item 15 / 顧客専用項目15 |
| 77 | TRSP_DVBR_COD | NVARCHAR2(14) | Y | Delivery Branch Code / 配達店コード |
| 78 | TRSP_DVBR_TEL | NVARCHAR2(30) | Y | Delivery Branch Tel / 配達店電話番号 |
| 79 | TRSP_COOL_KND | CHAR(1) | Y | Cool Kind / クール区分 |
| 80 | TRSP_OOHD_KND | CHAR(1) | Y | Out-Of-Home Delivery Kind / 自宅外配送区分 |
| 81 | TRSP_TSP_COD | NVARCHAR2(40) | Y | True Shipper Code / 真荷主コード |
| 82 | TRSP_YBBZ_NUM | NVARCHAR2(64) | Y | Yubin Biz Card customer number / ゆうびんビズカードお客様番号 |
| 83 | TRSP_SPDS_KND | NVARCHAR2(4) | Y | Shipping Date Setting Kind / 発送日設定区分 |
| 84 | TRSP_JPSP_TIM | CHAR(4) | Y | (JP)Shipping time / (JP)発送時刻 |
| 85 | TRSP_JPSC_COD | NVARCHAR2(8) | Y | (JP)Shipping company code / (JP)発送会社コード |
| 86 | TRSP_JPSD_COD | NVARCHAR2(12) | Y | (JP)Shipping department code / (JP)発送局コード |
| 87 | TRSP_MT_ZIP_KND | NVARCHAR2(4) | Y | （MEITETSU）Zip Store Pattern / (名鉄)郵便店所パターン |
| 88 | TRSP_MT_USR_ID | NVARCHAR2(30) | Y | （MEITETSU）User ID / (名鉄)ユーザID |
| 89 | TRSP_MT_USR_PASS | NVARCHAR2(1000) | Y | （MEITETSU）Password / (名鉄)パスワード |
| 90 | TRSP_MT_PAY_COD | NVARCHAR2(20) | Y | （MEITETSU）Payer Code / (名鉄)支払人コード |
| 91 | TRSP_MT_NX_CUST_COD | NVARCHAR2(38) | Y | （MEITETSU）NX Customer Code / (名鉄)NX顧客コード |
| 92 | TRSP_MT_STR_COD | NVARCHAR2(8) | Y | (MEITETSU)Store Code / (名鉄)発店所コード |
| 93 | TRSP_MT_STR_NAM | NVARCHAR2(100) | Y | (MEITETSU)Store Name / (名鉄)発店名略称 |
| 94 | TRSP_MT_STR_TEL | NVARCHAR2(40) | Y | (MEITETSU)Store Tel / (名鉄)発店電話番号 |
| 95 | TRSP_VWCAL_FLG | CHAR(1) | N | Volumetric Weight Calculation Flag / 容積重量計算フラグ |
