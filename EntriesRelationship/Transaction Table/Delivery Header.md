# GWH_TJ_DV_H
#Entity #Standard #OUTBOUND

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
| 18 | DVH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DVH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DVH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DVH_DLCT_NUM | CHAR(14) | N | Delivery Control Number / 配送管理番号 |
| 22 | DVH_SP_SCDL_YMD | CHAR(8) | N | Shipping Scheduled Date / 出荷予定日 |
| 23 | DVH_DV_SCDL_YMD | CHAR(8) | Y | Delivery Schduled Date / 納品予定日 |
| 24 | DVH_DV_SCDL_TIM | NVARCHAR2(12) | Y | Delivery Schduled Time / 納品予定時間 |
| 25 | DVH_SP_NUM | NVARCHAR2(12) | Y | Shipping Number / 出荷番号 |
| 26 | DVH_BTGP_NUM | NVARCHAR2(12) | Y | Batch Grouping Number / バッチグループ番号 |
| 27 | DVH_BT_NUM | NVARCHAR2(12) | Y | Batch Number / バッチ番号 |
| 28 | DVH_TP_NUM | NVARCHAR2(12) | Y | Total Picking Number / トータルピッキング番号 |
| 29 | DVH_DNPR_FLG | CHAR(1) | N | Delivery Note Printed Flag / 送り状発行フラグ |
| 30 | DVH_FDNP_FLG | CHAR(1) | N | First Delivery Note Print Flag / 初回送り状印刷フラグ |
| 31 | DVH_SLPR_FLG | CHAR(1) | N | Shipping Label Printed Flag / 荷札印刷フラグ |
| 32 | DVH_DLPR_FLG | CHAR(1) | N | Delivery List Printed Flag / 配送一覧表発行フラグ |
| 33 | DVH_CLP_FLG1 | CHAR(1) | N | Customized List Printed Flag 01 / 顧客専用リスト発行フラグ01 |
| 34 | DVH_CLP_FLG2 | CHAR(1) | N | Customized List Printed Flag 02 / 顧客専用リスト発行フラグ02 |
| 35 | DVH_CLP_FLG3 | CHAR(1) | N | Customized List Printed Flag 03 / 顧客専用リスト発行フラグ03 |
| 36 | DVH_CLP_FLG4 | CHAR(1) | N | Customized List Printed Flag 04 / 顧客専用リスト発行フラグ04 |
| 37 | DVH_CLP_FLG5 | CHAR(1) | N | Customized List Printed Flag 05 / 顧客専用リスト発行フラグ05 |
| 38 | DVH_SND_FLG | CHAR(1) | N | Delivery Send Flag / 配送送信フラグ |
| 39 | DVH_TRSP_COD | NVARCHAR2(10) | Y | Transport Mode / 輸送モード |
| 40 | DVH_TRSP_QTY | NUMBER(9) | Y | Transport Quantity / 輸送個数 |
| 41 | DVH_GRS_WGT | NUMBER(12,6) | Y | Gross Weight / 実重量 |
| 42 | DVH_M3 | NUMBER(12,6) | Y | Volume / 容積 |
| 43 | DVH_NMDN_NUM | NUMBER(3) | Y | Number Of Delivery Order / 送り状枚数 |
| 44 | DVH_NMSL_NUM | NUMBER(9) | Y | Number Of Shipping Label / 荷札枚数 |
| 45 | DVH_DN_FIL | NVARCHAR2(1000) | Y | Delivery Note Remarks / 送り状備考 |
| 46 | DVH_DCP_YMD | CHAR(8) | Y | Delivered Complete Date / 配達完了日 |
| 47 | DVH_DCP_TIM | CHAR(4) | Y | Delivered Complete Time / 配達完了時間 |
| 48 | DVH_PROD_AMN | NUMBER(14,4) | Y | Payment For Product / 品代金 |
| 49 | DVH_CSD_AMN | NUMBER(14,4) | Y | Cash on Delivery Commodity Price / 代引品代 |
| 50 | DVH_CSDV_AMN | NUMBER(14,4) | Y | Cash on Delivery VAT Amount / 代引品代消費税 |
| 51 | DVH_MDN_NUM | NVARCHAR2(80) | Y | Delivery Order Number / 代表送り状番号 |
| 52 | DVH_INDT_KND | NVARCHAR2(4) | Y | Industry Kind / 業種区分 |
| 53 | DVH_ITCS_COD | NVARCHAR2(26) | Y | IT Customer Code / IT顧客コード |
| 54 | DVH_SRT1_COD | NVARCHAR2(20) | Y | Sorting Code1 / 仕分番号1 |
| 55 | DVH_SRT2_COD | NVARCHAR2(20) | Y | Sorting Code2 / 仕分番号2 |
| 56 | DVH_SRT3_COD | NVARCHAR2(20) | Y | Sorting Code3 / 仕分番号3 |
| 57 | DVH_SRT4_COD | NVARCHAR2(20) | Y | Sorting Code4 / 仕分番号4 |
| 58 | DVH_DVBR_COD | NVARCHAR2(12) | Y | Delivery Branch Code / 配達店コード |
| 59 | DVH_DVBR_NAM1 | NVARCHAR2(60) | Y | Delivery Branch Name1 / 配達店名称１－漢字 |
| 60 | DVH_DVBR_NAM2 | NVARCHAR2(60) | Y | Delivery Branch Name2 / 配達店名称２－漢字 |
| 61 | DVH_SDBR_NAM | NVARCHAR2(32) | Y | Delivery Branch Name Short / 配達店略称 |
| 62 | DVH_DVBR_TEL | NVARCHAR2(30) | Y | Delivery Branch Tel / 配達店電話番号 |
| 63 | DVH_SP_JIS | NVARCHAR2(22) | Y | Shipper Jis / 荷送人住所JIS |
| 64 | DVH_DV_JIS | NVARCHAR2(22) | Y | Delivery Jis / 届け先住所JIS |
| 65 | DVH_DVTM_KND | NVARCHAR2(8) | Y | Delivery Time Kind / お届け指定時間帯区分 |
| 66 | DVH_CHG_WGT | NUMBER(8,3) | Y | Chargeable Weight / 計算重量 |
| 67 | DVH_JXCS_COD | NVARCHAR2(26) | Y | JPEX Customer Code / ＪＰＥｘ顧客コード |
| 68 | DVH_INS_AMN | NUMBER(14,4) | Y | Insurance Amount / 保険金額 |
| 69 | DVH_SPDL_FLG | CHAR(1) | N | SPEED File Download Flag / N:未ダウンロード、Y:ダウンロード済 |
| 70 | DVH_SJDL_FLG | CHAR(1) | N | SPEED JZN file Download Flag / N:未ダウンロード、Y:ダウンロード済 |
| 71 | DVH_YMDL_FLG | CHAR(1) | N | Yamato B2 File Download Flag / ヤマトB2ファイルダウンロードフラグ |
| 72 | DVH_SGDL_FLG | CHAR(1) | N | Sagawa eHiden file Download Flag / 佐川e飛伝ファイルダウンロードフラグ |
| 73 | DVH_STFG1_FLG | CHAR(1) | N | Reserve Flag 01 / 予備フラグ01 |
| 74 | DVH_STFG2_FLG | CHAR(1) | N | Reserve Flag 02 / 予備フラグ02 |
| 75 | DVH_STFG3_FLG | CHAR(1) | N | Reserve Flag 03 / 予備フラグ03 |
| 76 | DVH_STFG4_FLG | CHAR(1) | N | Reserve Flag 04 / 予備フラグ04 |
| 77 | DVH_STFG5_FLG | CHAR(1) | N | Reserve Flag 05 / 予備フラグ05 |
| 78 | DVH_STFG6_FLG | CHAR(1) | N | Reserve Flag 06 / 予備フラグ06 |
| 79 | DVH_STFG7_FLG | CHAR(1) | N | Reserve Flag 07 / 予備フラグ07 |
| 80 | DVH_STFG8_FLG | CHAR(1) | N | Reserve Flag 08 / 予備フラグ08 |
| 81 | DVH_STFG9_FLG | CHAR(1) | N | Reserve Flag 09 / 予備フラグ09 |
| 82 | DVH_STFG10_FLG | CHAR(1) | N | Reserve Flag 10 / 予備フラグ10 |
| 83 | DVH_CFG1_FLG | CHAR(1) | N | Customize Reserve Flag 01 / 顧客専用予備フラグ01 |
| 84 | DVH_CFG2_FLG | CHAR(1) | N | Customize Reserve Flag 02 / 顧客専用予備フラグ02 |
| 85 | DVH_CFG3_FLG | CHAR(1) | N | Customize Reserve Flag 03 / 顧客専用予備フラグ03 |
| 86 | DVH_CFG4_FLG | CHAR(1) | N | Customize Reserve Flag 04 / 顧客専用予備フラグ04 |
| 87 | DVH_CFG5_FLG | CHAR(1) | N | Customize Reserve Flag 05 / 顧客専用予備フラグ05 |
| 88 | DVH_CFG6_FLG | CHAR(1) | N | Customize Reserve Flag 06 / 顧客専用予備フラグ06 |
| 89 | DVH_CFG7_FLG | CHAR(1) | N | Customize Reserve Flag 07 / 顧客専用予備フラグ07 |
| 90 | DVH_CFG8_FLG | CHAR(1) | N | Customize Reserve Flag 08 / 顧客専用予備フラグ08 |
| 91 | DVH_CFG9_FLG | CHAR(1) | N | Customize Reserve Flag 09 / 顧客専用予備フラグ09 |
| 92 | DVH_CFG10_FLG | CHAR(1) | N | Customize Reserve Flag 10 / 顧客専用予備フラグ10 |
| 93 | DVH_STFG11_FLG | CHAR(1) | Y | Reserve Flag 11 / 予備フラグ11 |
| 94 | DVH_STFG12_FLG | CHAR(1) | Y | Reserve Flag 12 / 予備フラグ12 |
| 95 | DVH_STFG13_FLG | CHAR(1) | Y | Reserve Flag 13 / 予備フラグ13 |
| 96 | DVH_STFG14_FLG | CHAR(1) | Y | Reserve Flag 14 / 予備フラグ14 |
| 97 | DVH_STFG15_FLG | CHAR(1) | Y | Reserve Flag 15 / 予備フラグ15 |
| 98 | DVH_CFG11_FLG | CHAR(1) | Y | Customize Reserve Flag 11 / 顧客専用予備フラグ11 |
| 99 | DVH_CFG12_FLG | CHAR(1) | Y | Customize Reserve Flag 12 / 顧客専用予備フラグ12 |
| 100 | DVH_CFG13_FLG | CHAR(1) | Y | Customize Reserve Flag 13 / 顧客専用予備フラグ13 |
| 101 | DVH_CFG14_FLG | CHAR(1) | Y | Customize Reserve Flag 14 / 顧客専用予備フラグ14 |
| 102 | DVH_CFG15_FLG | CHAR(1) | Y | Customize Reserve Flag 15 / 顧客専用予備フラグ15 |
| 103 | DVH_DLV_INST_FLG | CHAR(1) | N | Delivery Installed Flag / 配送情報登録判定フラグ |
| 104 | DVH_IV_NUM | NVARCHAR2(100) | Y | Invoice Number / 伝票番号 |
| 105 | DVH_RF_NUM | NVARCHAR2(100) | Y | Customer Ref# / 顧客リファレンスNo |
| 106 | DVH_PO_NUM | NVARCHAR2(100) | Y | P/O Number / P/O No |
| 107 | DVH_DLV_RMKS | NVARCHAR2(1000) | Y | Remarks for Delivery Note / 送り状備考 |
| 108 | DVH_DLV_COD | NVARCHAR2(80) | Y | Delivery To Code / 配送先コード |
| 109 | DVH_DLV_NAM1 | NVARCHAR2(100) | Y | Delivery To Name1 / 配送先名称1 |
| 110 | DVH_DLV_NAM2 | NVARCHAR2(100) | Y | Delivery To Name2 / 配送先名称2 |
| 111 | DVH_DLV_JIS | NVARCHAR2(22) | Y | Delivery To Jis / 配送先住所コード |
| 112 | DVH_DLV_ADR1 | NVARCHAR2(100) | Y | Delivery To Address1 / 配送先住所1 |
| 113 | DVH_DLV_ADR2 | NVARCHAR2(100) | Y | Delivery To Address2 / 配送先住所2 |
| 114 | DVH_DLV_ADR3 | NVARCHAR2(100) | Y | Delivery To Address3 / 配送先住所3 |
| 115 | DVH_DLV_ADR4 | NVARCHAR2(100) | Y | Delivery To Address4 / 配送先住所4 |
| 116 | DVH_DLV_ADR5 | NVARCHAR2(100) | Y | Delivery To Address5 / 配送先住所5 |
| 117 | DVH_DLV_ZIP | NVARCHAR2(20) | Y | Delivery To Zip / 配送先郵便番号 |
| 118 | DVH_DLV_TEL | NVARCHAR2(40) | Y | Delivery To Tel / 配送先電話番号 |
| 119 | DVH_SP_COD | NVARCHAR2(80) | Y | Shipper Code / 荷送人コード |
| 120 | DVH_SP_NAM1 | NVARCHAR2(100) | Y | Shipper Name1 / 荷送人名称1 |
| 121 | DVH_SP_NAM2 | NVARCHAR2(100) | Y | Shipper Name2 / 荷送人名称2 |
| 122 | DVH_SP_ADR1 | NVARCHAR2(100) | Y | Shipper Address1 / 荷送人住所1 |
| 123 | DVH_SP_ADR2 | NVARCHAR2(100) | Y | Shipper Address2 / 荷送人住所2 |
| 124 | DVH_SP_ADR3 | NVARCHAR2(100) | Y | Shipper Address3 / 荷送人住所3 |
| 125 | DVH_SP_ADR4 | NVARCHAR2(100) | Y | Shipper Address4 / 荷送人住所4 |
| 126 | DVH_SP_ADR5 | NVARCHAR2(100) | Y | Shipper Address5 / 荷送人住所5 |
| 127 | DVH_SP_ZIP | NVARCHAR2(20) | Y | Shipper Zip / 荷送人郵便番号 |
| 128 | DVH_SP_TEL | NVARCHAR2(40) | Y | Shipper Tel / 荷送人電話番号 |
| 129 | DVH_TRSP_CNFM_COD | NVARCHAR2(10) | Y | Transport Type Code (Confirmation) / 輸送手段（実績） |
| 130 | DVH_ROUT_CNFM_COD | NVARCHAR2(10) | Y | Route Code (Confirmation) / ルートコード（実績） |
| 131 | DVH_CAR_CNFM_NUM | NVARCHAR2(24) | Y | Car Number (Confirmation) / 車番（実績） |
| 132 | DVH_STI1 | NVARCHAR2(100) | Y | Reserve Item 01 / 予備項目01 |
| 133 | DVH_STI2 | NVARCHAR2(100) | Y | Reserve Item 02 / 予備項目02 |
| 134 | DVH_STI3 | NVARCHAR2(100) | Y | Reserve Item 03 / 予備項目03 |
| 135 | DVH_STI4 | NVARCHAR2(100) | Y | Reserve Item 04 / 予備項目04 |
| 136 | DVH_STI5 | NVARCHAR2(100) | Y | Reserve Item 05 / 予備項目05 |
| 137 | DVH_STI6 | NVARCHAR2(100) | Y | Reserve Item 06 / 予備項目06 |
| 138 | DVH_STI7 | NVARCHAR2(100) | Y | Reserve Item 07 / 予備項目07 |
| 139 | DVH_STI8 | NVARCHAR2(100) | Y | Reserve Item 08 / 予備項目08 |
| 140 | DVH_STI9 | NVARCHAR2(100) | Y | Reserve Item 09 / 予備項目09 |
| 141 | DVH_STI10 | NVARCHAR2(100) | Y | Reserve Item 10 / 予備項目10 |
| 142 | DVH_STI11 | NVARCHAR2(100) | Y | Reserve Item 11 / 予備項目11 |
| 143 | DVH_STI12 | NVARCHAR2(100) | Y | Reserve Item 12 / 予備項目12 |
| 144 | DVH_STI13 | NVARCHAR2(100) | Y | Reserve Item 13 / 予備項目13 |
| 145 | DVH_STI14 | NVARCHAR2(100) | Y | Reserve Item 14 / 予備項目14 |
| 146 | DVH_STI15 | NVARCHAR2(100) | Y | Reserve Item 15 / 予備項目15 |
| 147 | DVH_CIT1 | NVARCHAR2(100) | Y | Customized Item 01 / 顧客専用項目01 |
| 148 | DVH_CIT2 | NVARCHAR2(100) | Y | Customized Item 02 / 顧客専用項目02 |
| 149 | DVH_CIT3 | NVARCHAR2(100) | Y | Customized Item 03 / 顧客専用項目03 |
| 150 | DVH_CIT4 | NVARCHAR2(100) | Y | Customized Item 04 / 顧客専用項目04 |
| 151 | DVH_CIT5 | NVARCHAR2(100) | Y | Customized Item 05 / 顧客専用項目05 |
| 152 | DVH_CIT6 | NVARCHAR2(100) | Y | Customized Item 06 / 顧客専用項目06 |
| 153 | DVH_CIT7 | NVARCHAR2(100) | Y | Customized Item 07 / 顧客専用項目07 |
| 154 | DVH_CIT8 | NVARCHAR2(100) | Y | Customized Item 08 / 顧客専用項目08 |
| 155 | DVH_CIT9 | NVARCHAR2(100) | Y | Customized Item 09 / 顧客専用項目09 |
| 156 | DVH_CIT10 | NVARCHAR2(100) | Y | Customized Item 10 / 顧客専用項目10 |
| 157 | DVH_CIT11 | NVARCHAR2(100) | Y | Customized Item 11 / 顧客専用項目11 |
| 158 | DVH_CIT12 | NVARCHAR2(100) | Y | Customized Item 12 / 顧客専用項目12 |
| 159 | DVH_CIT13 | NVARCHAR2(100) | Y | Customized Item 13 / 顧客専用項目13 |
| 160 | DVH_CIT14 | NVARCHAR2(100) | Y | Customized Item 14 / 顧客専用項目14 |
| 161 | DVH_CIT15 | NVARCHAR2(100) | Y | Customized Item 15 / 顧客専用項目15 |
| 162 | DVH_DLV_CTRY_COD | NVARCHAR2(4) | Y | Delivery To Country Code / 配送先国コード |
| 163 | DVH_DLV_CITY_COD | NVARCHAR2(6) | Y | Delivery To City Code / 配送先都市コード |
| 164 | DVH_DLV_CITY_NAM | NVARCHAR2(100) | Y | Delivery To City Name / 配送先都市名 |
| 165 | DVH_DLV_STAT_COD | NVARCHAR2(4) | Y | Delivery To State Code / 配送先州コード |
| 166 | DVH_SP_CTRY_COD | NVARCHAR2(4) | Y | Shipper Country Code / 荷送人国コード |
| 167 | DVH_SP_CITY_COD | NVARCHAR2(6) | Y | Shipper City Code / 荷送人都市コード |
| 168 | DVH_SP_CITY_NAM | NVARCHAR2(100) | Y | Shipper City Name / 荷送人都市名 |
| 169 | DVH_SP_STAT_COD | NVARCHAR2(4) | Y | Shipper State Code / 荷送人州コード |
| 170 | DVH_YMSD_FLG | CHAR(1) | N | YAMATO Send Flag / ヤマト送信フラグ |
| 171 | DVH_FDL_FLG | CHAR(1) | N | File Download Flag / ファイルダウンロードフラグ |
