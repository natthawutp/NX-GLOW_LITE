# GWH_TJ_LD_H
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
| 18 | LDH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LDH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LDH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LDH_LD_NUM | NVARCHAR2(12) | N | Loading Number / 積込番号 |
| 22 | LDH_SCDL_YMD | DATE | N | Loading Scheduled Date / 積込予定日 |
| 23 | LDH_LOAD_STS | NVARCHAR2(6) | N | Loading Status / 積込ステータス |
| 24 | LDH_LOAD_YMD | DATE | Y | Loaded Date / 積込完了日 |
| 25 | LDH_CNF_FLG | CHAR(1) | N | Loaded Confirmation Flag / 積込確定フラグ |
| 26 | LDH_CNF_YMD | DATE | Y | Loaded Confirmation Date / 積込確定日 |
| 27 | LDH_SND_CNF_FLG | CHAR(1) | N | Send Confirmation Flag / 確定送信フラグ |
| 28 | LDH_SND_CNF_YMD | DATE | Y | Send Confirmation Date / 確定送信日 |
| 29 | LDH_RF_NUM | NVARCHAR2(100) | Y | Customer Ref# / 顧客リファレンスNo |
| 30 | LDH_WHS_RMKS | NVARCHAR2(1000) | Y | Remarks for Warehouse / 倉庫向け作業指示 |
| 31 | LDH_SOD_RMKS | NVARCHAR2(1000) | Y | Remarks for Statement of Delivery / 納品書備考 |
| 32 | LDH_WGT | NUMBER(12,6) | N | Total Weight / 合計重量 |
| 33 | LDH_M3 | NUMBER(12,6) | N | Total Volume / 合計容積 |
| 34 | LDH_RWGT | NUMBER(12,6) | Y | Weight(Result.) / 実績重量 |
| 35 | LDH_RM3 | NUMBER(12,6) | Y | Volume(Result.) / 実績容積 |
| 36 | LDH_DLV_COD | NVARCHAR2(80) | Y | Delivery To Code / 配送先コード |
| 37 | LDH_DLV_NAM1 | NVARCHAR2(100) | Y | Delivery To Name1 / 配送先名称1 |
| 38 | LDH_DLV_NAM2 | NVARCHAR2(100) | Y | Delivery To Name2 / 配送先名称2 |
| 39 | LDH_DLV_JIS | NVARCHAR2(22) | Y | Delivery To Jis / 配送先住所コード |
| 40 | LDH_DLV_ADR1 | NVARCHAR2(100) | Y | Delivery To Address1 / 配送先住所1 |
| 41 | LDH_DLV_ADR2 | NVARCHAR2(100) | Y | Delivery To Address2 / 配送先住所2 |
| 42 | LDH_DLV_ADR3 | NVARCHAR2(100) | Y | Delivery To Address3 / 配送先住所3 |
| 43 | LDH_DLV_ADR4 | NVARCHAR2(100) | Y | Delivery To Address4 / 配送先住所4 |
| 44 | LDH_DLV_ADR5 | NVARCHAR2(100) | Y | Delivery To Address5 / 配送先住所5 |
| 45 | LDH_DLV_ZIP | NVARCHAR2(20) | Y | Delivery To Zip / 配送先郵便番号 |
| 46 | LDH_DLV_TEL | NVARCHAR2(40) | Y | Delivery To Tel / 配送先電話番号 |
| 47 | LDH_FNL_DLV_COD | NVARCHAR2(80) | Y | Final Delivery To Code / 最終配送先コード |
| 48 | LDH_FNL_DLV_NAM1 | NVARCHAR2(100) | Y | Final Deliver To Name1 / 最終配送先名称1 |
| 49 | LDH_FNL_DLV_NAM2 | NVARCHAR2(100) | Y | Final Deliver To Name2 / 最終配送先名称2 |
| 50 | LDH_FNL_DLV_JIS | NVARCHAR2(22) | Y | Final Delivery To Jis / 最終配送先住所コード |
| 51 | LDH_FNL_DLV_ADR1 | NVARCHAR2(100) | Y | Final Delivery To Address1 / 最終配送先住所1 |
| 52 | LDH_FNL_DLV_ADR2 | NVARCHAR2(100) | Y | Final Delivery To Address2 / 最終配送先住所2 |
| 53 | LDH_FNL_DLV_ADR3 | NVARCHAR2(100) | Y | Final Delivery To Address3 / 最終配送先住所3 |
| 54 | LDH_FNL_DLV_ADR4 | NVARCHAR2(100) | Y | Final Delivery To Address4 / 最終配送先住所4 |
| 55 | LDH_FNL_DLV_ADR5 | NVARCHAR2(100) | Y | Final Delivery To Address5 / 最終配送先住所5 |
| 56 | LDH_FNL_DLV_ZIP | NVARCHAR2(20) | Y | Final Deliver To Zip / 最終配送先郵便番号 |
| 57 | LDH_FNL_DLV_TEL | NVARCHAR2(40) | Y | Final Deliver To Tel / 最終配送先電話番号 |
| 58 | LDH_SP_COD | NVARCHAR2(80) | Y | Shipper Code / 荷送人コード |
| 59 | LDH_SP_NAM1 | NVARCHAR2(100) | Y | Shipper Name1 / 荷送人名称1 |
| 60 | LDH_SP_NAM2 | NVARCHAR2(100) | Y | Shipper Name2 / 荷送人名称2 |
| 61 | LDH_SP_JIS | NVARCHAR2(22) | Y | Shipper Jis / 荷送人住所コード |
| 62 | LDH_SP_ADR1 | NVARCHAR2(100) | Y | Shipper Address1 / 荷送人住所1 |
| 63 | LDH_SP_ADR2 | NVARCHAR2(100) | Y | Shipper Address2 / 荷送人住所2 |
| 64 | LDH_SP_ADR3 | NVARCHAR2(100) | Y | Shipper Address3 / 荷送人住所3 |
| 65 | LDH_SP_ADR4 | NVARCHAR2(100) | Y | Shipper Address4 / 荷送人住所4 |
| 66 | LDH_SP_ADR5 | NVARCHAR2(100) | Y | Shipper Address5 / 荷送人住所5 |
| 67 | LDH_SP_ZIP | NVARCHAR2(20) | Y | Shipper Zip / 荷送人郵便番号 |
| 68 | LDH_SP_TEL | NVARCHAR2(40) | Y | Shipper Tel / 荷送人電話番号 |
| 69 | LDH_CUST_BRNC_COD | NVARCHAR2(40) | Y | Customer Branch Code / 顧客支店コード |
| 70 | LDH_CUST_BRNC_NAM | NVARCHAR2(100) | Y | Customer Branch Name / 顧客支店名称 |
| 71 | LDH_CUST_STFF_NAM | NVARCHAR2(100) | Y | Customer Staff / 顧客担当者 |
| 72 | LDH_DLV_SCDL_YMD | DATE | Y | Delivery Schduled Date / 納品予定日 |
| 73 | LDH_DLV_SCDL_TIM | CHAR(6) | Y | Delivery Schduled Time / 納品予定時間 |
| 74 | LDH_TRSP_COD | NVARCHAR2(10) | Y | Transport Type Code / 輸送手段 |
| 75 | LDH_ROUT_COD | NVARCHAR2(10) | Y | Route Code / ルートコード |
| 76 | LDH_CAR_NUM | NVARCHAR2(60) | Y | Car/Contianer No. / 車番/コンテナ番号 |
| 77 | LDH_GT_NUM | NVARCHAR2(60) | Y | Gate No. / ゲートNo. |
| 78 | LDH_LPR_FLG | CHAR(1) | N | Loading Instruction Printed Flag / 積込指示書発行フラグ |
| 79 | LDH_LRPR_FLG | CHAR(1) | N | Loading Instruction Result Printed Flag / 積込書発行フラグ |
| 80 | LDH_CLP_FLG1 | CHAR(1) | N | Customized List Printed Flag 01 / 顧客専用リスト発行フラグ01 |
| 81 | LDH_CLP_FLG2 | CHAR(1) | N | Customized List Printed Flag 02 / 顧客専用リスト発行フラグ02 |
| 82 | LDH_CLP_FLG3 | CHAR(1) | N | Customized List Printed Flag 03 / 顧客専用リスト発行フラグ03 |
| 83 | LDH_CLP_FLG4 | CHAR(1) | N | Customized List Printed Flag 04 / 顧客専用リスト発行フラグ04 |
| 84 | LDH_CLP_FLG5 | CHAR(1) | N | Customized List Printed Flag 05 / 顧客専用リスト発行フラグ05 |
| 85 | LDH_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 86 | LDH_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 87 | LDH_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 88 | LDH_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 89 | LDH_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 90 | LDH_CLI6 | NVARCHAR2(100) | Y | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| 91 | LDH_CLI7 | NVARCHAR2(100) | Y | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| 92 | LDH_CLI8 | NVARCHAR2(100) | Y | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| 93 | LDH_CLI9 | NVARCHAR2(100) | Y | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| 94 | LDH_EDI_FLG | CHAR(1) | N | EDI Flag / EDI登録フラグ |
| 95 | LDH_RCV_BTCH_NUM | NVARCHAR2(60) | Y | Recive Batch Number / 受信バッチNo |
| 96 | LDH_SND_BTCH_NUM | NVARCHAR2(60) | Y | Send Batch Number / 送信バッチNo |
| 97 | LDH_SLD_QTY | NUMBER(12,3) | N | Loaded QTY(Sched.) / 積込予定数 |
| 98 | LDH_RLD_QTY | NUMBER(12,3) | Y | Loaded QTY(Result.) / 積込実績数 |
