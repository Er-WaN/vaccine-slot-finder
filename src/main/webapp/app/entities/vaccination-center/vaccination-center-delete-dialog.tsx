import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './vaccination-center.reducer';

export interface IVaccinationCenterDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VaccinationCenterDeleteDialog = (props: IVaccinationCenterDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/vaccination-center');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.vaccinationCenterEntity.id);
  };

  const { vaccinationCenterEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="vaccinationCenterDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="vaccineSlotFinderApp.vaccinationCenter.delete.question">
        Are you sure you want to delete this VaccinationCenter?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-vaccinationCenter" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ vaccinationCenter }: IRootState) => ({
  vaccinationCenterEntity: vaccinationCenter.entity,
  updateSuccess: vaccinationCenter.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationCenterDeleteDialog);
