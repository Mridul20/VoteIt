package com.example.onlinevotingsystem.constants;

public class HashMapConstants {

    public static final String FETCH_PARAM_TYPE_KEY="FetchType";
    public static final String FETCH_RESULT_TYPE_KEY="ResultType";

    //For Authentication of User, Admin and Officer
    public static final String FETCH_TYPE_LOGIN_USER="LoginUser";
    public static final String FETCH_TYPE_LOGIN_ADMIN="LoginAdmin";
    public static final String FETCH_TYPE_LOGIN_OFFICER="LoginOfficer";

    public static final String FETCH_PARAM_LOGIN_USERNAME_KEY="Username";
    public static final String FETCH_PARAM_LOGIN_PASSWORD_KEY="Password";

    public static final String FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY="IsLoginSuccessful";

    //For Verifying the Phone Number
    public static final String FETCH_TYPE_VERIFY_PHONE_NUM="VerifyPhoneNum";

    public static final String FETCH_PARAM_VERIFY_PHONE_NUM_VOTER_ID_KEY="VoterID";
    public static final String FETCH_PARAM_VERIFY_PHONE_NUM_NUMBER_KEY="PhoneNumber";
    public static final String FETCH_PARAM_VERIFY_PHONE_NUM_ROLE_KEY="VerifyRole";

    public static final String FETCH_RESULT_VERIFY_PHONE_NUM_KEY="IsNumberVerified";
    public static final String FETCH_RESULT_VERIFY_PHONE_NUM_IS_REG_KEY="IsRegistered";

    //For Checking if Voter ID Exists or Not
    public static final String FETCH_TYPE_CHECK_VOTER_ID="CheckVoterID";

    public static final String FETCH_PARAM_CHECK_VOTER_ID_KEY="VoterID";

    public static final String FETCH_RESULT_CHECK_VOTER_ID_EXISTS_KEY="VoterIdExists";

    //For Fetching Data of a User with a Particular Voter ID
    public static final String FETCH_TYPE_EXISTING_DATA_FROM_ID="ExistingDataFromVoterID";

    public static final String FETCH_PARAM_EXISTING_DATA_FROM_ID_KEY="VoterID";

    public static final String FETCH_RESULT_EXISTING_DATA_FROM_ID_USER_KEY="ResultUser";

    //For Checking If Username is Unique or Not
    public static final String FETCH_TYPE_USER_CHECK_UNIQUE_USERNAME="CheckUserUniqueUsername";
    public static final String FETCH_TYPE_ADMIN_CHECK_UNIQUE_USERNAME="CheckAdminUniqueUsername";
    public static final String FETCH_TYPE_OFFICER_CHECK_UNIQUE_USERNAME="CheckOfficerUniqueUsername";

    public static final String FETCH_PARAM_CHECK_UNIQUE_USERNAME_KEY="UniqueUsername";

    public static final String FETCH_RESULT_CHECK_UNIQUE_USERNAME_KEY="IsUsernameUnique";

    //For Fetching Details of a Particular Admin
    public static final String FETCH_TYPE_ADMIN_DETAILS="AdminDetails";

    public static final String FETCH_PARAM_ADMIN_DETAILS_USERNAME_KEY="AdminUsername";

    public static final String FETCH_RESULT_ADMIN_DETAILS_KEY="AdminDetails";

    //For Fetching Details of a Particular Officer
    public static final String FETCH_TYPE_OFFICER_DETAILS="OfficerDetails";

    public static final String FETCH_PARAM_OFFICER_DETAILS_USERNAME_KEY="OfficerUsername";

    public static final String FETCH_RESULT_OFFICER_DETAILS_KEY="OfficerDetails";

    //For Fetching List of Officers
    public static final String FETCH_TYPE_OFFICER_LIST="GetOfficersList";

    public static final String FETCH_RESULT_OFFICERS_LIST_KEY="OfficersList";

    //For Fetching List of Polls
    public static final String FETCH_TYPE_POLL_LIST="GetPollList";

    public static final String FETCH_RESULT_POLL_LIST_KEY="PollList";

    //For Fetching Details of a Particular Poll
    public static final String FETCH_TYPE_POLL_DETAILS="GetPollDetailsFromNumber";

    public static final String FETCH_PARAM_POLL_DETAILS_POLL_NUM_KEY="PollNumber";

    public static final String FETCH_RESULT_POLL_DETAILS_KEY="PollDetails";

    //For Fetching the Election Result of a Particular Poll
    public static final String FETCH_TYPE_POLL_RESULT="GetPollResult";

    public static final String FETCH_PARAM_POLL_RESULT_POLL_NUM_KEY="PollNumber";

    public static final String FETCH_RESULT_POLL_RESULT_CANDIDATE_LIST_KEY="PollResult";

    //For Fetching List of Unassigned Polls
    public static final String FETCH_TYPE_UNASSIGNED_POLLS="GetUnassignedPolls";

    public static final String FETCH_RESULT_UNASSIGNED_POLLS_KEY="UnassignedPollsList";

    //For Fetching Addresses of all the Polls
    public static final String FETCH_TYPE_POLLS_ADDRESS="GetPollsAddressList";

    public static final String FETCH_RESULT_POLLS_ADDRESS_KEY="PollsAddressList";

    //For Fetching the List of all the Users of a Particular Poll
    public static final String FETCH_TYPE_POLL_VOTERS_LIST="GetPollVotersList";

    public static final String FETCH_PARAM_POLL_VOTERS_LIST_POLL_NUM_KEY="PollNum";

    public static final String FETCH_RESULT_POLL_VOTERS_LIST_KEY="PollVotersList";

    //For Fetching the List of All the Users
    public static final String FETCH_TYPE_ALL_VOTERS_LIST="GetAllVotersList";

    public static final String FETCH_RESULT_ALL_VOTERS_LIST_KEY="AllVotersList";


    public static final String FETCH_RESULT_SUCCESS_KEY="IsFetchSuccess";
    public static final String FETCH_RESULT_ERROR_KEY="ResultError";

    public static final String UPDATE_TYPE_KEY="UpdateType";

    //For Adding a New Admin
    public static final String UPDATE_TYPE_ADD_ADMIN="AddAdmin";

    public static final String UPDATE_PARAM_ADMIN_KEY="InputAdmin";

    //For Updating Admin Password
    public static final String UPDATE_TYPE_ADMIN_PASSWORD="UpdateAdminPassword";

    public static final String UPDATE_PARAM_ADMIN_USERNAME_KEY="AdminUsername";
    public static final String UPDATE_PARAM_ADMIN_PASSWORD_KEY="AdminPassword";

    //For Updating Admin Photo
    public static final String UPDATE_TYPE_ADMIN_PHOTO="UpdateAdminPhoto";

    public static final String UPDATE_PARAM_ADMIN_PHOTO_USERNAME_KEY="AdminUsername";
    public static final String UPDATE_PARAM_ADMIN_PHOTO_KEY="AdminPhoto";

    //For Adding a New Officer
    public static final String UPDATE_TYPE_ADD_OFFICER="AddOfficer";

    public static final String UPDATE_PARAM_OFFICER_KEY="InputOfficer";

    //For Updating Officer Password
    public static final String UPDATE_TYPE_OFFICER_PASSWORD="UpdateOfficerPassword";

    public static final String UPDATE_PARAM_OFFICER_USERNAME_KEY="OfficerUsername";
    public static final String UPDATE_PARAM_OFFICER_PASSWORD_KEY="OfficerPassword";

    //For Updating Officer Photo
    public static final String UPDATE_TYPE_OFFICER_PHOTO="UpdateOfficerPhoto";

    public static final String UPDATE_PARAM_OFFICER_PHOTO_USERNAME_KEY="OfficerUsername";
    public static final String UPDATE_PARAM_OFFICER_PHOTO_KEY="OfficerPhoto";

    //For Assigning a Particular Poll to an Officer
    public static final String UPDATE_TYPE_OFFICER_POLL="UpdateOfficerPoll";

    public static final String UPDATE_PARAM_OFFICER_POLL_USERNAME_KEY="OfficerUsername";
    public static final String UPDATE_PARAM_OFFICER_POLL_KEY="OfficerPoll";

    //For Adding a new Poll
    public static final String UPDATE_TYPE_ADD_POLL="AddPoll";

    public static final String UPDATE_PARAM_POLL_ADDRESS_KEY="PollAddress";
    public static final String UPDATE_PARAM_POLL_ELECTION_START_TIME_KEY="ElectionStartTime";
    public static final String UPDATE_PARAM_POLL_ELECTION_END_TIME_KEY="ElectionEndTime";

    //For Updating the Election Time of a Poll
    public static final String UPDATE_TYPE_POLL_ELECTION_TIME="ElectionTime";

    public static final String UPDATE_PARAM_POLL_ELECTION_TIME_POLL_NUM_KEY="PollNumber";
    public static final String UPDATE_PARAM_POLL_ELECTION_TIME_START_KEY="ElectionStartTime";
    public static final String UPDATE_PARAM_POLL_ELECTION_TIME_END_KEY="ElectionEndTime";

    //For Adding a New User
    public static final String UPDATE_TYPE_ADD_USER="AddUser";

    public static final String UPDATE_PARAM_ADD_USER_KEY="InputUser";

    //For Updating Password of a Given User
    public static final String UPDATE_TYPE_VOTER_PASSWORD="UpdateVoterPassword";

    public static final String UPDATE_PARAM_VOTER_ID_KEY="VoterID";
    public static final String UPDATE_PARAM_VOTER_PASSWORD_KEY="VoterPassword";

    //For Updating Photo of a Given User
    public static final String UPDATE_TYPE_VOTER_PHOTO="UpdateVoterPhoto";

    public static final String UPDATE_PARAM_VOTER_PHOTO_ID_KEY="VoterID";
    public static final String UPDATE_PARAM_VOTER_PHOTO_KEY="VoterPhoto";

    //For Casting a Vote
    public static final String UPDATE_TYPE_CAST_VOTE="CastVote";

    public static final String UPDATE_PARAM_CAST_VOTE_VOTER_ID_KEY="VoterId";
    public static final String UPDATE_PARAM_CAST_VOTE_CANDIDATE_ID_KEY="CandidateId";
    public static final String UPDATE_PARAM_CAST_VOTE_POLL_NUM_KEY="PollNum";

    //For Adding a New Candidate
    public static final String UPDATE_TYPE_ADD_CANDIDATE="AddCandidate";

    public static final String UPDATE_PARAM_CANDIDATE_KEY="InputCandidate";

    //For Updating the Details of the Candidate
    public static final String UPDATE_TYPE_UPDATE_CANDIDATE="UpdateCandidateDetails";

    public static final String UPDATE_PARAM_UPDATE_CANDIDATE_ID_KEY="CandidateID";
    public static final String UPDATE_PARAM_UPDATE_CANDIDATE_NAME_KEY="CandidateName";
    public static final String UPDATE_PARAM_UPDATE_CANDIDATE_PHONE_NUM_KEY="CandidatePhoneNum";
    public static final String UPDATE_PARAM_UPDATE_CANDIDATE_DOB_KEY="CandidateDob";
    public static final String UPDATE_PARAM_UPDATE_CANDIDATE_SYMBOL_NAME_KEY="CandidateSymbol";

    //For Updating the Photo of a Candidate
    public static final String UPDATE_TYPE_CANDIDATE_PHOTO="UpdateCandidatePhoto";

    public static final String UPDATE_PARAM_CANDIDATE_PHOTO_ID_KEY="CandidateId";
    public static final String UPDATE_PARAM_CANDIDATE_PHOTO_URL_KEY="PhotoUrl";

    //For Updating the Election Symbol Photo of a Candidate
    public static final String UPDATE_TYPE_CANDIDATE_SYMBOL_PHOTO="UpdateCandidateSymbolPhoto";

    public static final String UPDATE_PARAM_CANDIDATE_SYMBOL_PHOTO_ID_KEY="CandidateId";
    public static final String UPDATE_PARAM_CANDIDATE_SYMBOL_PHOTO_URL_KEY="PhotoUrl";

    //For Deleting an Existing Candidate
    public static final String UPDATE_TYPE_DELETE_CANDIDATE="DeleteCandidate";

    public static final String UPDATE_PARAM_DELETE_CANDIDATE_ID_KEY="CandidateId";
    public static final String UPDATE_PARAM_DELETE_CANDIDATE_POLL_NUM_KEY="PollNum";

    //For Updating Voters Table while Registering User
    public static final String UPDATE_TYPE_REGISTER_USER="RegisterUser";

    public static final String UPDATE_PARAM_REGISTER_USER_VOTER_ID="VoterID";
    public static final String UPDATE_PARAM_REGISTER_USER_PASSWORD="Password";

    //For Removing Photo
    public static final String UPDATE_TYPE_REMOVE_PHOTO="RemovePhoto";

    public static final String UPDATE_PARAM_REMOVE_PHOTO_ROLE_KEY="RemovePhotoRole";
    public static final String UPDATE_PARAM_REMOVE_PHOTO_ID_KEY="RemovePhotoId";
}
