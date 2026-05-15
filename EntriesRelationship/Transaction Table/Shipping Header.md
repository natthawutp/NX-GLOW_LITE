# GWH_TJ_SP_H
#Entity #Standard #OUTBOUND

| COLUMN_NAME          | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                                      |
|----------------------|---------------------|----------|--------------|-----------|---------------------------------------------------------------|
| DEL_FLG              | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                         |
| CRT_YMD              | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                                  |
| CRT_TIM              | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                                 |
| CRT_TMID             | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                       |
| CRT_USER             | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                                      |
| CRT_PGM              | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                                 |
| CRT_TM_ZONE          | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                           |
| CRT_YMDHMS           | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）                      |
| CRT_L_YMDHMS         | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）                   |
| UPD_YMD              | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                                  |
| UPD_TIM              | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                                 |
| UPD_TMID             | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                                   |
| UPD_USER             | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                                      |
| UPD_PGM              | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                                 |
| UPD_TM_ZONE          | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                           |
| UPD_YMDHMS           | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）                      |
| UPD_L_YMDHMS         | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）                   |
| SPH_CPNY_COD         | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                       |
| SPH_WHS_COD          | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                        |
| SPH_CUST_COD         | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                         |
| SPH_SP_NUM           | NVARCHAR2(6 CHAR)   | No       |              | 21        | Shipping Number / 出荷番号                                        |
| SPH_SCDL_YMD         | DATE                | No       |              | 22        | Shipping Scheduled Date / 出荷予定日                               |
| SPH_TRN_KND          | NVARCHAR2(3 CHAR)   | No       |              | 23        | Transaction Kind / 伝票区分                                       |
| SPH_SP_STS           | NVARCHAR2(3 CHAR)   | No       |              | 24        | Shipping Status / 出荷ステータス                                     |
| SPH_SHIP_FLG         | CHAR(1 BYTE)        | No       |              | 25        | Shipped Flag / 出荷フラグ                                          |
| SPH_SHIP_YMD         | DATE                | Yes      |              | 26        | Shipped Date / 出荷日                                            |
| SPH_CNOP_YMD         | DATE                | Yes      |              | 27        | Shipping Confirmation Operation Date / 出荷確定処理日                |
| SPH_BT_NUM           | NVARCHAR2(6 CHAR)   | Yes      |              | 28        | Batch Number / バッチ番号                                          |
| SPH_BTGP_NUM         | NVARCHAR2(6 CHAR)   | Yes      |              | 29        | Batch Grouping Number / バッチグループ番号                             |
| SPH_TP_NUM           | NVARCHAR2(6 CHAR)   | Yes      |              | 30        | Total Picking Number / トータルピッキング番号                            |
| SPH_IV_NUM           | NVARCHAR2(50 CHAR)  | Yes      |              | 31        | Invoice Number / 伝票番号                                         |
| SPH_RF_NUM           | NVARCHAR2(50 CHAR)  | Yes      |              | 32        | Customer Ref# / 顧客リファレンスNo                                    |
| SPH_PO_NUM           | NVARCHAR2(50 CHAR)  | Yes      |              | 33        | P/O Number / P/O No                                           |
| SPH_WHS_RMKS         | NVARCHAR2(500 CHAR) | Yes      |              | 34        | Remarks for Warehouse / 倉庫向け作業指示                              |
| SPH_SOD_RMKS         | NVARCHAR2(500 CHAR) | Yes      |              | 35        | Remarks for Statement of Delivery / 納品書備考                     |
| SPH_DLV_RMKS         | NVARCHAR2(500 CHAR) | Yes      |              | 36        | Remarks for Delivery Note / 送り状備考                             |
| SPH_WGT              | NUMBER(12,6)        | No       |              | 37        | Total Weight / 合計重量                                           |
| SPH_M3               | NUMBER(12,6)        | No       |              | 38        | Total Volume / 合計容積                                           |
| SPH_DLV_COD          | NVARCHAR2(40 CHAR)  | Yes      |              | 39        | Delivery To Code / 配送先コード                                     |
| SPH_DLV_NAM1         | NVARCHAR2(50 CHAR)  | Yes      |              | 40        | Delivery To Name1 / 配送先名称1                                    |
| SPH_DLV_NAM2         | NVARCHAR2(50 CHAR)  | Yes      |              | 41        | Delivery To Name2 / 配送先名称2                                    |
| SPH_DLV_JIS          | NVARCHAR2(11 CHAR)  | Yes      |              | 42        | Delivery To Jis / 配送先住所コード                                    |
| SPH_DLV_ADR1         | NVARCHAR2(50 CHAR)  | Yes      |              | 43        | Delivery To Address1 / 配送先住所1                                 |
| SPH_DLV_ADR2         | NVARCHAR2(50 CHAR)  | Yes      |              | 44        | Delivery To Address2 / 配送先住所2                                 |
| SPH_DLV_ADR3         | NVARCHAR2(50 CHAR)  | Yes      |              | 45        | Delivery To Address3 / 配送先住所3                                 |
| SPH_DLV_ADR4         | NVARCHAR2(50 CHAR)  | Yes      |              | 46        | Delivery To Address4 / 配送先住所4                                 |
| SPH_DLV_ADR5         | NVARCHAR2(50 CHAR)  | Yes      |              | 47        | Delivery To Address5 / 配送先住所5                                 |
| SPH_DLV_ZIP          | NVARCHAR2(10 CHAR)  | Yes      |              | 48        | Delivery To Zip / 配送先郵便番号                                     |
| SPH_DLV_TEL          | NVARCHAR2(20 CHAR)  | Yes      |              | 49        | Delivery To Tel / 配送先電話番号                                     |
| SPH_FNL_DLV_COD      | NVARCHAR2(40 CHAR)  | Yes      |              | 50        | Final Delivery To Code / 最終配送先コード                             |
| SPH_FNL_DLV_NAM1     | NVARCHAR2(50 CHAR)  | Yes      |              | 51        | Final Deliver To Name1 / 最終配送先名称1                             |
| SPH_FNL_DLV_NAM2     | NVARCHAR2(50 CHAR)  | Yes      |              | 52        | Final Deliver To Name2 / 最終配送先名称2                             |
| SPH_FNL_DLV_JIS      | NVARCHAR2(11 CHAR)  | Yes      |              | 53        | Final Delivery To Jis / 最終配送先住所コード                            |
| SPH_FNL_DLV_ADR1     | NVARCHAR2(50 CHAR)  | Yes      |              | 54        | Final Delivery To Address1 / 最終配送先住所1                         |
| SPH_FNL_DLV_ADR2     | NVARCHAR2(50 CHAR)  | Yes      |              | 55        | Final Delivery To Address2 / 最終配送先住所2                         |
| SPH_FNL_DLV_ADR3     | NVARCHAR2(50 CHAR)  | Yes      |              | 56        | Final Delivery To Address3 / 最終配送先住所3                         |
| SPH_FNL_DLV_ADR4     | NVARCHAR2(50 CHAR)  | Yes      |              | 57        | Final Delivery To Address4 / 最終配送先住所4                         |
| SPH_FNL_DLV_ADR5     | NVARCHAR2(50 CHAR)  | Yes      |              | 58        | Final Delivery To Address5 / 最終配送先住所5                         |
| SPH_FNL_DLV_ZIP      | NVARCHAR2(10 CHAR)  | Yes      |              | 59        | Final Deliver To Zip / 最終配送先郵便番号                              |
| SPH_FNL_DLV_TEL      | NVARCHAR2(20 CHAR)  | Yes      |              | 60        | Final Deliver To Tel / 最終配送先電話番号                              |
| SPH_SP_COD           | NVARCHAR2(40 CHAR)  | Yes      |              | 61        | Shipper Code / 荷送人コード                                         |
| SPH_SP_NAM1          | NVARCHAR2(50 CHAR)  | Yes      |              | 62        | Shipper Name1 / 荷送人名称1                                        |
| SPH_SP_NAM2          | NVARCHAR2(50 CHAR)  | Yes      |              | 63        | Shipper Name2 / 荷送人名称2                                        |
| SPH_SP_JIS           | NVARCHAR2(11 CHAR)  | Yes      |              | 64        | Shipper Jis / 荷送人住所コード                                        |
| SPH_SP_ADR1          | NVARCHAR2(50 CHAR)  | Yes      |              | 65        | Shipper Address1 / 荷送人住所1                                     |
| SPH_SP_ADR2          | NVARCHAR2(50 CHAR)  | Yes      |              | 66        | Shipper Address2 / 荷送人住所2                                     |
| SPH_SP_ADR3          | NVARCHAR2(50 CHAR)  | Yes      |              | 67        | Shipper Address3 / 荷送人住所3                                     |
| SPH_SP_ADR4          | NVARCHAR2(50 CHAR)  | Yes      |              | 68        | Shipper Address4 / 荷送人住所4                                     |
| SPH_SP_ADR5          | NVARCHAR2(50 CHAR)  | Yes      |              | 69        | Shipper Address5 / 荷送人住所5                                     |
| SPH_SP_ZIP           | NVARCHAR2(10 CHAR)  | Yes      |              | 70        | Shipper Zip / 荷送人郵便番号                                         |
| SPH_SP_TEL           | NVARCHAR2(20 CHAR)  | Yes      |              | 71        | Shipper Tel / 荷送人電話番号                                         |
| SPH_BIL_COD          | NVARCHAR2(40 CHAR)  | Yes      |              | 72        | Charge Billing Code / 請求先コード                                  |
| SPH_BIL_NAM1         | NVARCHAR2(50 CHAR)  | Yes      |              | 73        | Charge Billing Name1 / 請求先名称1                                 |
| SPH_BIL_NAM2         | NVARCHAR2(50 CHAR)  | Yes      |              | 74        | Charge Billing Name2 / 請求先名称2                                 |
| SPH_BIL_JIS          | NVARCHAR2(11 CHAR)  | Yes      |              | 75        | Charge Billing Jis / 請求先住所コード                                 |
| SPH_BIL_ADR1         | NVARCHAR2(50 CHAR)  | Yes      |              | 76        | Charge Billing Address1 / 請求先住所1                              |
| SPH_BIL_ADR2         | NVARCHAR2(50 CHAR)  | Yes      |              | 77        | Charge Billing Address2 / 請求先住所2                              |
| SPH_BIL_ADR3         | NVARCHAR2(50 CHAR)  | Yes      |              | 78        | Charge Billing Address3 / 請求先住所3                              |
| SPH_BIL_ADR4         | NVARCHAR2(50 CHAR)  | Yes      |              | 79        | Charge Billing Address4 / 請求先住所4                              |
| SPH_BIL_ADR5         | NVARCHAR2(50 CHAR)  | Yes      |              | 80        | Charge Billing Address5 / 請求先住所5                              |
| SPH_BIL_ZIP          | NVARCHAR2(10 CHAR)  | Yes      |              | 81        | Charge Billing Zip / 請求先郵便番号                                  |
| SPH_BIL_TEL          | NVARCHAR2(20 CHAR)  | Yes      |              | 82        | Charge Billing Tel / 請求先電話番号                                  |
| SPH_CUST_BRNC_COD    | NVARCHAR2(20 CHAR)  | Yes      |              | 83        | Customer Branch Code / 顧客支店コード                                |
| SPH_CUST_BRNC_NAM    | NVARCHAR2(50 CHAR)  | Yes      |              | 84        | Customer Branch Name / 顧客支店名称                                 |
| SPH_CUST_STFF_NAM    | NVARCHAR2(50 CHAR)  | Yes      |              | 85        | Customer Staff / 顧客担当者                                        |
| SPH_DLV_SCDL_YMD     | DATE                | Yes      |              | 86        | Delivery Schduled Date / 納品予定日                                |
| SPH_DLV_SCDL_TIM     | CHAR(6 BYTE)        | Yes      |              | 87        | Delivery Schduled Time / 納品予定時間                               |
| SPH_TRSP_SCDL_COD    | NVARCHAR2(5 CHAR)   | Yes      |              | 88        | Transport Type Code (Schduled) / 輸送手段（予定）                     |
| SPH_TRSP_CNFM_COD    | NVARCHAR2(5 CHAR)   | Yes      |              | 89        | Transport Type Code (Confirmation) / 輸送手段（実績）                 |
| SPH_ROUT_SCDL_COD    | NVARCHAR2(5 CHAR)   | Yes      |              | 90        | Route Code (Schduled) / ルートコード（予定）                            |
| SPH_ROUT_CNFM_COD    | NVARCHAR2(5 CHAR)   | Yes      |              | 91        | Route Code (Confirmation) / ルートコード（実績）                        |
| SPH_CAR_SCDL_NUM     | NVARCHAR2(12 CHAR)  | Yes      |              | 92        | Car Number (Schduled) / 車番（予定）                                |
| SPH_CAR_CNFM_NUM     | NVARCHAR2(12 CHAR)  | Yes      |              | 93        | Car Number (Confirmation) / 車番（実績）                            |
| SPH_TRSP_QTY         | NUMBER(9,0)         | No       |              | 94        | Transport Quantity / 輸送個数                                     |
| SPH_DLCT_NUM         | CHAR(14 BYTE)       | Yes      |              | 95        | Delivery Control Number / 配送管理番号                              |
| SPH_PTDV_FLG         | CHAR(1 BYTE)        | No       |              | 96        | Partial Delivery Flag / 分納フラグ                                 |
| SPH_OGSP_NUM         | NVARCHAR2(6 CHAR)   | Yes      |              | 97        | Original Shipping No. / 元出荷番号                                 |
| SPH_BOND_FLG         | CHAR(1 BYTE)        | Yes      |              | 98        | Bond Flag / 保税フラグ                                             |
| SPH_BL_NUM           | NVARCHAR2(20 CHAR)  | Yes      |              | 99        | B/L No / B/L No                                               |
| SPH_CT_NUM           | NVARCHAR2(30 CHAR)  | Yes      |              | 100       | Container No. / コンテナNo                                        |
| SPH_SPPR_FLG         | CHAR(1 BYTE)        | No       |              | 101       | Single Picking List Printed Flag / シングルピッキングリスト発行フラグ          |
| SPH_TPPR_FLG         | CHAR(1 BYTE)        | No       |              | 102       | Total Picking List Printed Flag / トータルピッキングリスト発行フラグ           |
| SPH_PLPR_FLG         | CHAR(1 BYTE)        | No       |              | 103       | Picking Label Printed Flag / ピッキングラベル発行フラグ                    |
| SPH_DNPR_FLG         | CHAR(1 BYTE)        | No       |              | 104       | Delivery Note Printed Flag / 送り状発行フラグ                         |
| SPH_STPR_FLG         | CHAR(1 BYTE)        | No       |              | 105       | Shipping Tag Printed Flag / 荷札発行フラグ                           |
| SPH_SDPR_FLG         | CHAR(1 BYTE)        | No       |              | 106       | Statement of Delivery Printed Flag / 納品書発行フラグ                 |
| SPH_PAPR_FLG         | CHAR(1 BYTE)        | No       |              | 107       | Packing List Printed Flag / 梱包リスト発行フラグ                        |
| SPH_DSPR_FLG         | CHAR(1 BYTE)        | No       |              | 108       | Document Shipping Printed Flag / 出庫伝票発行フラグ                    |
| SPH_CLP_FLG1         | CHAR(1 BYTE)        | No       |              | 109       | Customized List Printed Flag 01 / 顧客専用リスト発行フラグ01              |
| SPH_CLP_FLG2         | CHAR(1 BYTE)        | No       |              | 110       | Customized List Printed Flag 02 / 顧客専用リスト発行フラグ02              |
| SPH_CLP_FLG3         | CHAR(1 BYTE)        | No       |              | 111       | Customized List Printed Flag 03 / 顧客専用リスト発行フラグ03              |
| SPH_CLP_FLG4         | CHAR(1 BYTE)        | No       |              | 112       | Customized List Printed Flag 04 / 顧客専用リスト発行フラグ04              |
| SPH_CLP_FLG5         | CHAR(1 BYTE)        | No       |              | 113       | Customized List Printed Flag 05 / 顧客専用リスト発行フラグ05              |
| SPH_CLI1             | NVARCHAR2(50 CHAR)  | Yes      |              | 114       | Customized List Items for printing 01 / 顧客専用リスト印字用項目01        |
| SPH_CLI2             | NVARCHAR2(50 CHAR)  | Yes      |              | 115       | Customized List Items for printing 02 / 顧客専用リスト印字用項目02        |
| SPH_CLI3             | NVARCHAR2(50 CHAR)  | Yes      |              | 116       | Customized List Items for printing 03 / 顧客専用リスト印字用項目03        |
| SPH_CLI4             | NVARCHAR2(50 CHAR)  | Yes      |              | 117       | Customized List Items for printing 04 / 顧客専用リスト印字用項目04        |
| SPH_CLI5             | NVARCHAR2(50 CHAR)  | Yes      |              | 118       | Customized List Items for printing 05 / 顧客専用リスト印字用項目05        |
| SPH_CLI6             | NVARCHAR2(50 CHAR)  | Yes      |              | 119       | Customized List Items for printing 06 / 顧客専用リスト印字用項目06        |
| SPH_CLI7             | NVARCHAR2(50 CHAR)  | Yes      |              | 120       | Customized List Items for printing 07 / 顧客専用リスト印字用項目07        |
| SPH_CLI8             | NVARCHAR2(50 CHAR)  | Yes      |              | 121       | Customized List Items for printing 08 / 顧客専用リスト印字用項目08        |
| SPH_CLI9             | NVARCHAR2(50 CHAR)  | Yes      |              | 122       | Customized List Items for printing 09 / 顧客専用リスト印字用項目09        |
| SPH_EDI_FLG          | CHAR(1 BYTE)        | No       |              | 123       | EDI Flag / EDI登録フラグ                                           |
| SPH_RCV_BTCH_NUM     | NVARCHAR2(30 CHAR)  | Yes      |              | 124       | Recive Batch Number / 受信バッチNo                                 |
| SPH_SND_BTCH_NUM     | NVARCHAR2(30 CHAR)  | Yes      |              | 125       | Send Batch Number / 送信バッチNo                                   |
| SPH_SND_BTCH_YMD     | DATE                | Yes      |              | 126       | Send Batch Date / 送信バッチ日                                      |
| SPH_RWGT             | NUMBER(12,6)        | No       | '0'          | 127       | Weight(Result.) / 実績重量                                        |
| SPH_RM3              | NUMBER(12,6)        | No       | '0'          | 128       | Volume(Result.) / 実績容積                                        |
| SPH_CSD_AMN          | NUMBER(14,4)        | No       | '0'          | 129       | Cash on Delivery Commodity Price / 品代金                        |
| SPH_CSDV_AMN         | NUMBER(14,4)        | No       | '0'          | 130       | Cash on Delivery VAT Amount / 品代金消費税                          |
| SPH_CLI10            | NVARCHAR2(50 CHAR)  | Yes      |              | 131       | Customized List Items for printing 10 / 顧客専用リスト印字用項目10        |
| SPH_CLI11            | NVARCHAR2(50 CHAR)  | Yes      |              | 132       | Customized List Items for printing 11 / 顧客専用リスト印字用項目11        |
| SPH_CLI12            | NVARCHAR2(50 CHAR)  | Yes      |              | 133       | Customized List Items for printing 12 / 顧客専用リスト印字用項目12        |
| SPH_CLI13            | NVARCHAR2(50 CHAR)  | Yes      |              | 134       | Customized List Items for printing 13 / 顧客専用リスト印字用項目13        |
| SPH_CLI14            | NVARCHAR2(50 CHAR)  | Yes      |              | 135       | Customized List Items for printing 14 / 顧客専用リスト印字用項目14        |
| SPH_CLI15            | NVARCHAR2(50 CHAR)  | Yes      |              | 136       | Customized List Items for printing 15 / 顧客専用リスト印字用項目15        |
| SPH_CFG1_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 137       | Customized Flag 01 / 顧客専用フラグ01                                |
| SPH_CFG2_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 138       | Customized Flag 02 / 顧客専用フラグ02                                |
| SPH_CFG3_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 139       | Customized Flag 03 / 顧客専用フラグ03                                |
| SPH_CFG4_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 140       | Customized Flag 04 / 顧客専用フラグ04                                |
| SPH_CFG5_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 141       | Customized Flag 05 / 顧客専用フラグ05                                |
| SPH_CFG6_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 142       | Customized Flag 06 / 顧客専用フラグ06                                |
| SPH_CFG7_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 143       | Customized Flag 07 / 顧客専用フラグ07                                |
| SPH_CFG8_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 144       | Customized Flag 08 / 顧客専用フラグ08                                |
| SPH_CFG9_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 145       | Customized Flag 09 / 顧客専用フラグ09                                |
| SPH_CFG10_FLG        | CHAR(1 BYTE)        | No       | 'N'          | 146       | Customized Flag 10 / 顧客専用フラグ10                                |
| SPH_CFG11_FLG        | CHAR(1 BYTE)        | No       | 'N'          | 147       | Customized Flag 11 / 顧客専用フラグ11                                |
| SPH_CFG12_FLG        | CHAR(1 BYTE)        | No       | 'N'          | 148       | Customized Flag 12 / 顧客専用フラグ12                                |
| SPH_CFG13_FLG        | CHAR(1 BYTE)        | No       | 'N'          | 149       | Customized Flag 13 / 顧客専用フラグ13                                |
| SPH_CFG14_FLG        | CHAR(1 BYTE)        | No       | 'N'          | 150       | Customized Flag 14 / 顧客専用フラグ14                                |
| SPH_CFG15_FLG        | CHAR(1 BYTE)        | No       | 'N'          | 151       | Customized Flag 15 / 顧客専用フラグ15                                |
| SPH_CNI1_NUM         | NUMBER(9,0)         | Yes      |              | 152       | Customized Number Item 01 / 顧客専用数字項目01                        |
| SPH_CNI2_NUM         | NUMBER(9,0)         | Yes      |              | 153       | Customized Number Item 02 / 顧客専用数字項目02                        |
| SPH_CNI3_NUM         | NUMBER(9,0)         | Yes      |              | 154       | Customized Number Item 03 / 顧客専用数字項目03                        |
| SPH_CNI4_NUM         | NUMBER(9,0)         | Yes      |              | 155       | Customized Number Item 04 / 顧客専用数字項目04                        |
| SPH_CNI5_NUM         | NUMBER(9,0)         | Yes      |              | 156       | Customized Number Item 05 / 顧客専用数字項目05                        |
| SPH_CNI6_NUM         | NUMBER(9,0)         | Yes      |              | 157       | Customized Number Item 06 / 顧客専用数字項目06                        |
| SPH_CNI7_NUM         | NUMBER(9,0)         | Yes      |              | 158       | Customized Number Item 07 / 顧客専用数字項目07                        |
| SPH_CNI8_NUM         | NUMBER(9,0)         | Yes      |              | 159       | Customized Number Item 08 / 顧客専用数字項目08                        |
| SPH_CNI9_NUM         | NUMBER(9,0)         | Yes      |              | 160       | Customized Number Item 09 / 顧客専用数字項目09                        |
| SPH_CNI10_NUM        | NUMBER(14,4)        | Yes      |              | 161       | Customized Number Item 10 / 顧客専用数字項目10                        |
| SPH_CNI11_NUM        | NUMBER(14,4)        | Yes      |              | 162       | Customized Number Item 11 / 顧客専用数字項目11                        |
| SPH_CNI12_NUM        | NUMBER(14,4)        | Yes      |              | 163       | Customized Number Item 12 / 顧客専用数字項目12                        |
| SPH_CNI13_NUM        | NUMBER(14,4)        | Yes      |              | 164       | Customized Number Item 13 / 顧客専用数字項目13                        |
| SPH_CNI14_NUM        | NUMBER(14,4)        | Yes      |              | 165       | Customized Number Item 14 / 顧客専用数字項目14                        |
| SPH_CNI15_NUM        | NUMBER(14,4)        | Yes      |              | 166       | Customized Number Item 15 / 顧客専用数字項目15                        |
| SPH_TMS_FLG          | CHAR(1 BYTE)        | No       | 'N'          | 167       | TMS Flag / TMSフラグ                                             |
| SPH_COI1             | NVARCHAR2(100 CHAR) | Yes      |              | 168       | Customized Only Item 01 / カスタマイズ項目01                          |
| SPH_COI2             | NVARCHAR2(100 CHAR) | Yes      |              | 169       | Customized Only Item 02 / カスタマイズ項目02                          |
| SPH_COI3             | NVARCHAR2(100 CHAR) | Yes      |              | 170       | Customized Only Item 03 / カスタマイズ項目03                          |
| SPH_COI4             | NVARCHAR2(100 CHAR) | Yes      |              | 171       | Customized Only Item 04 / カスタマイズ項目04                          |
| SPH_COI5             | NVARCHAR2(100 CHAR) | Yes      |              | 172       | Customized Only Item 05 / カスタマイズ項目05                          |
| SPH_COI6             | NVARCHAR2(100 CHAR) | Yes      |              | 173       | Customized Only Item 06 / カスタマイズ項目06                          |
| SPH_COI7             | NVARCHAR2(100 CHAR) | Yes      |              | 174       | Customized Only Item 07 / カスタマイズ項目07                          |
| SPH_COI8             | NVARCHAR2(100 CHAR) | Yes      |              | 175       | Customized Only Item 08 / カスタマイズ項目08                          |
| SPH_COI9             | NVARCHAR2(100 CHAR) | Yes      |              | 176       | Customized Only Item 09 / カスタマイズ項目09                          |
| SPH_COI10            | NVARCHAR2(100 CHAR) | Yes      |              | 177       | Customized Only Item 10 / カスタマイズ項目10                          |
| SPH_COI11            | NVARCHAR2(100 CHAR) | Yes      |              | 178       | Customized Only Item 11 / カスタマイズ項目11                          |
| SPH_COI12            | NVARCHAR2(100 CHAR) | Yes      |              | 179       | Customized Only Item 12 / カスタマイズ項目12                          |
| SPH_COI13            | NVARCHAR2(100 CHAR) | Yes      |              | 180       | Customized Only Item 13 / カスタマイズ項目13                          |
| SPH_COI14            | NVARCHAR2(100 CHAR) | Yes      |              | 181       | Customized Only Item 14 / カスタマイズ項目14                          |
| SPH_COI15            | NVARCHAR2(100 CHAR) | Yes      |              | 182       | Customized Only Item 15 / カスタマイズ項目15                          |
| SPH_COI16            | NVARCHAR2(100 CHAR) | Yes      |              | 183       | Customized Only Item 16 / カスタマイズ項目16                          |
| SPH_COI17            | NVARCHAR2(100 CHAR) | Yes      |              | 184       | Customized Only Item 17 / カスタマイズ項目17                          |
| SPH_COI18            | NVARCHAR2(100 CHAR) | Yes      |              | 185       | Customized Only Item 18 / カスタマイズ項目18                          |
| SPH_COI19            | NVARCHAR2(100 CHAR) | Yes      |              | 186       | Customized Only Item 19 / カスタマイズ項目19                          |
| SPH_COI20            | NVARCHAR2(100 CHAR) | Yes      |              | 187       | Customized Only Item 20 / カスタマイズ項目20                          |
| SPH_COI21            | NVARCHAR2(100 CHAR) | Yes      |              | 188       | Customized Only Item 21 / カスタマイズ項目21                          |
| SPH_COI22            | NVARCHAR2(100 CHAR) | Yes      |              | 189       | Customized Only Item 22 / カスタマイズ項目22                          |
| SPH_COI23            | NVARCHAR2(100 CHAR) | Yes      |              | 190       | Customized Only Item 23 / カスタマイズ項目23                          |
| SPH_COI24            | NVARCHAR2(100 CHAR) | Yes      |              | 191       | Customized Only Item 24 / カスタマイズ項目24                          |
| SPH_COI25            | NVARCHAR2(100 CHAR) | Yes      |              | 192       | Customized Only Item 25 / カスタマイズ項目25                          |
| SPH_COI26            | NVARCHAR2(100 CHAR) | Yes      |              | 193       | Customized Only Item 26 / カスタマイズ項目26                          |
| SPH_COI27            | NVARCHAR2(100 CHAR) | Yes      |              | 194       | Customized Only Item 27 / カスタマイズ項目27                          |
| SPH_COI28            | NVARCHAR2(100 CHAR) | Yes      |              | 195       | Customized Only Item 28 / カスタマイズ項目28                          |
| SPH_COI29            | NVARCHAR2(100 CHAR) | Yes      |              | 196       | Customized Only Item 29 / カスタマイズ項目29                          |
| SPH_COI30            | NVARCHAR2(100 CHAR) | Yes      |              | 197       | Customized Only Item 30 / カスタマイズ項目30                          |
| SPH_COI31            | NVARCHAR2(100 CHAR) | Yes      |              | 198       | Customized Only Item 31 / カスタマイズ項目31                          |
| SPH_COI32            | NVARCHAR2(100 CHAR) | Yes      |              | 199       | Customized Only Item 32 / カスタマイズ項目32                          |
| SPH_COI33            | NVARCHAR2(100 CHAR) | Yes      |              | 200       | Customized Only Item 33 / カスタマイズ項目33                          |
| SPH_COI34            | NVARCHAR2(100 CHAR) | Yes      |              | 201       | Customized Only Item 34 / カスタマイズ項目34                          |
| SPH_COI35            | NVARCHAR2(100 CHAR) | Yes      |              | 202       | Customized Only Item 35 / カスタマイズ項目35                          |
| SPH_COI36            | NVARCHAR2(100 CHAR) | Yes      |              | 203       | Customized Only Item 36 / カスタマイズ項目36                          |
| SPH_COI37            | NVARCHAR2(100 CHAR) | Yes      |              | 204       | Customized Only Item 37 / カスタマイズ項目37                          |
| SPH_COI38            | NVARCHAR2(100 CHAR) | Yes      |              | 205       | Customized Only Item 38 / カスタマイズ項目38                          |
| SPH_COI39            | NVARCHAR2(100 CHAR) | Yes      |              | 206       | Customized Only Item 39 / カスタマイズ項目39                          |
| SPH_COI40            | NVARCHAR2(100 CHAR) | Yes      |              | 207       | Customized Only Item 40 / カスタマイズ項目40                          |
| SPH_COIL1            | NVARCHAR2(255 CHAR) | Yes      |              | 208       | Customized Only Item (Long) 01 / カスタマイズ項目(long)01             |
| SPH_COIL2            | NVARCHAR2(255 CHAR) | Yes      |              | 209       | Customized Only Item (Long) 02 / カスタマイズ項目(long)02             |
| SPH_COIL3            | NVARCHAR2(255 CHAR) | Yes      |              | 210       | Customized Only Item (Long) 03 / カスタマイズ項目(long)03             |
| SPH_COIL4            | NVARCHAR2(255 CHAR) | Yes      |              | 211       | Customized Only Item (Long) 04 / カスタマイズ項目(long)04             |
| SPH_COIL5            | NVARCHAR2(255 CHAR) | Yes      |              | 212       | Customized Only Item (Long) 05 / カスタマイズ項目(long)05             |
| SPH_COIL6            | NVARCHAR2(255 CHAR) | Yes      |              | 213       | Customized Only Item (Long) 06 / カスタマイズ項目(long)06             |
| SPH_COIL7            | NVARCHAR2(255 CHAR) | Yes      |              | 214       | Customized Only Item (Long) 07 / カスタマイズ項目(long)07             |
| SPH_COIL8            | NVARCHAR2(255 CHAR) | Yes      |              | 215       | Customized Only Item (Long) 08 / カスタマイズ項目(long)08             |
| SPH_COIL9            | NVARCHAR2(255 CHAR) | Yes      |              | 216       | Customized Only Item (Long) 09 / カスタマイズ項目(long)09             |
| SPH_COIL10           | NVARCHAR2(255 CHAR) | Yes      |              | 217       | Customized Only Item (Long) 10 / カスタマイズ項目(long)10             |
| SPH_DLV_CTRY_COD     | NVARCHAR2(2 CHAR)   | Yes      |              | 218       | Delivery To Country Code / 配送先国コード                            |
| SPH_DLV_CITY_COD     | NVARCHAR2(3 CHAR)   | Yes      |              | 219       | Delivery To City Code / 配送先都市コード                              |
| SPH_DLV_CITY_NAM     | NVARCHAR2(50 CHAR)  | Yes      |              | 220       | Delivery To City Name / 配送先都市名                                |
| SPH_DLV_STAT_COD     | NVARCHAR2(2 CHAR)   | Yes      |              | 221       | Delivery To State Code / 配送先州コード                              |
| SPH_FNL_DLV_CTRY_COD | NVARCHAR2(2 CHAR)   | Yes      |              | 222       | Final Delivery To Country Code / 最終配送先国コード                    |
| SPH_FNL_DLV_CITY_COD | NVARCHAR2(3 CHAR)   | Yes      |              | 223       | Final Delivery To City Code / 最終配送先都市コード                      |
| SPH_FNL_DLV_CITY_NAM | NVARCHAR2(50 CHAR)  | Yes      |              | 224       | Final Delivery To City Name / 最終配送先都市名                        |
| SPH_FNL_DLV_STAT_COD | NVARCHAR2(2 CHAR)   | Yes      |              | 225       | Final Delivery To State Code / 最終配送先州コード                      |
| SPH_SP_CTRY_COD      | NVARCHAR2(2 CHAR)   | Yes      |              | 226       | Shipper Country Code / 荷送人国コード                                |
| SPH_SP_CITY_COD      | NVARCHAR2(3 CHAR)   | Yes      |              | 227       | Shipper City Code / 荷送人都市コード                                  |
| SPH_SP_CITY_NAM      | NVARCHAR2(50 CHAR)  | Yes      |              | 228       | Shipper City Name / 荷送人都市名                                    |
| SPH_SP_STAT_COD      | NVARCHAR2(2 CHAR)   | Yes      |              | 229       | Shipper State Code / 荷送人州コード                                  |
| SPH_BIL_CTRY_COD     | NVARCHAR2(2 CHAR)   | Yes      |              | 230       | Charge Billing Country Code / 請求先国コード                         |
| SPH_BIL_CITY_COD     | NVARCHAR2(3 CHAR)   | Yes      |              | 231       | Charge Billing City Code / 請求先都市コード                           |
| SPH_BIL_CITY_NAM     | NVARCHAR2(50 CHAR)  | Yes      |              | 232       | Charge Billing City Name / 請求先都市名                             |
| SPH_BIL_STAT_COD     | NVARCHAR2(2 CHAR)   | Yes      |              | 233       | Charge Billing State Code / 請求先州コード                           |
| SPH_QCCP_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 234       | QC Processing Completed Flag                                  |
| SPH_STCP_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 235       | Sortting Processing Completed Flag                            |
| SPH_CNF_MAIL_ADR     | NVARCHAR2(500 CHAR) | Yes      |              | 236       | Email address for sending confirmation(TO) / 実績送信先メールアドレス(TO) |
| SPH_CNEE_COD         | NVARCHAR2(12 CHAR)  | Yes      |              | 237       | Consignee Code / 荷受人コード                                       |
| SPH_NEML_ADR         | NVARCHAR2(60 CHAR)  | Yes      |              | 238       | Notice E-mail Address / 通知e-mailアドレス                          |
| SPH_OCPR_FLG         | CHAR(1 BYTE)        | No       | 'N'          | 239       | Order Check List Printed Flag / 出荷オーダーチェックリスト発行フラグ            |
